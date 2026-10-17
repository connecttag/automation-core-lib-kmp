package org.connecttag.lib.automation.core.engine

import org.connecttag.lib.automation.core.enums.MatchStrategy
import org.connecttag.lib.automation.core.enums.MatchType
import org.connecttag.lib.automation.core.model.RuleMatch

/**
 * Metadata contract representing a network packet or flow observation.
 */
public interface PacketFlowMetadata {
    public val saddr: String?
    public val daddr: String?
    public val sport: Int
    public val dport: Int
    public val protocol: Int
}

/**
 * Default implementation of [PacketFlowMetadata].
 */
public data class PacketFlow(
    override val saddr: String? = null,
    override val daddr: String? = null,
    override val sport: Int = 0,
    override val dport: Int = 0,
    override val protocol: Int = 6
) : PacketFlowMetadata

/**
 * Pure, high-performance rule matcher for network packets and domain names.
 *
 * Evaluates [RuleMatch] conditions against network metadata (Domain, IP, Port, Protocol, CIDR Subnets).
 * Completely decoupled from storage, scheduling, or UI layers.
 */
public class NetworkRuleMatcher(
    private val warningLogger: ((String, Throwable?) -> Unit)? = null
) {

    /**
     * Checks whether the given [packet] and optional [domain] match the [rule].
     */
    public fun matches(packet: PacketFlowMetadata, domain: String?, rule: RuleMatch): Boolean {
        return when (rule) {
            is RuleMatch.All -> true
            is RuleMatch.Single -> evaluateSingle(packet, domain, rule.value, rule.type, rule.strategy)
            is RuleMatch.Group -> rule.values.any { valItem ->
                evaluateSingle(packet, domain, valItem, rule.type, rule.strategy)
            }
        }
    }

    private fun evaluateSingle(
        packet: PacketFlowMetadata,
        domain: String?,
        pattern: String,
        type: MatchType,
        strategy: MatchStrategy
    ): Boolean {
        if (pattern.isBlank()) return false

        return when (type) {
            MatchType.ALL -> true

            MatchType.DOMAIN -> {
                val d = domain.orEmpty()
                if (d.isEmpty()) return false
                matchDomain(d, pattern, strategy)
            }

            MatchType.IP -> {
                val ip = packet.daddr.orEmpty()
                if (ip.isEmpty()) return false
                checkTextMatch(ip, pattern, strategy)
            }

            MatchType.CIDR -> {
                val ip = packet.daddr.orEmpty()
                if (ip.isEmpty()) return false
                matchCidr(ip, pattern)
            }

            MatchType.PORT -> {
                val targetPort = pattern.trim().toIntOrNull() ?: return false
                packet.dport == targetPort
            }

            MatchType.PORT_RANGE -> {
                matchPortRange(packet.dport, pattern)
            }

            MatchType.PROTOCOL -> {
                matchProtocol(packet.protocol, pattern)
            }

            MatchType.KEYWORD -> {
                val d = domain.orEmpty()
                val ip = packet.daddr.orEmpty()
                d.contains(pattern, ignoreCase = true) || ip.contains(pattern, ignoreCase = true)
            }
        }
    }

    private fun matchDomain(domain: String, pattern: String, strategy: MatchStrategy): Boolean {
        // Handle wildcard prefixes like *.example.com
        if (pattern.startsWith("*.")) {
            val basePattern = pattern.removePrefix("*.").lowercase()
            val lowerDomain = domain.lowercase()
            return lowerDomain == basePattern || lowerDomain.endsWith(".$basePattern")
        }
        return checkTextMatch(domain, pattern, strategy)
    }

    private fun matchPortRange(dport: Int, rangePattern: String): Boolean {
        val parts = if (rangePattern.contains("-")) {
            rangePattern.split("-")
        } else if (rangePattern.contains("..")) {
            rangePattern.split("..")
        } else {
            return false
        }
        if (parts.size != 2) return false
        val start = parts[0].trim().toIntOrNull() ?: return false
        val end = parts[1].trim().toIntOrNull() ?: return false
        val min = minOf(start, end)
        val max = maxOf(start, end)
        return dport in min..max
    }

    private fun matchProtocol(protocol: Int, pattern: String): Boolean {
        val normalized = pattern.trim().uppercase()
        return when (normalized) {
            "TCP", "6" -> protocol == 6
            "UDP", "17" -> protocol == 17
            "ICMP", "1" -> protocol == 1
            "ICMPV6", "58" -> protocol == 58
            else -> protocol.toString() == normalized
        }
    }

    private fun checkTextMatch(text: String, pattern: String, strategy: MatchStrategy): Boolean {
        return when (strategy) {
            MatchStrategy.EXACT -> text.equals(pattern, ignoreCase = true)
            MatchStrategy.CONTAINS -> text.contains(pattern, ignoreCase = true)
            MatchStrategy.STARTS_WITH -> text.startsWith(pattern, ignoreCase = true)
            MatchStrategy.ENDS_WITH -> text.endsWith(pattern, ignoreCase = true)
            MatchStrategy.REGEX -> try {
                Regex(pattern).containsMatchIn(text)
            } catch (e: Exception) {
                warningLogger?.invoke("Invalid regex pattern: $pattern", e)
                false
            }
        }
    }

    /**
     * Checks whether an IPv4 address falls within a CIDR subnet (e.g. 192.168.1.0/24 or 10.0.0.0/8).
     */
    public fun matchCidr(ipStr: String, cidrStr: String): Boolean {
        return try {
            val parts = cidrStr.trim().split("/")
            if (parts.size != 2) return false
            val subnetIpStr = parts[0].trim()
            val prefixLen = parts[1].trim().toIntOrNull() ?: return false

            if (prefixLen < 0 || prefixLen > 32) return false

            val ipLong = ipv4ToLong(ipStr) ?: return false
            val subnetLong = ipv4ToLong(subnetIpStr) ?: return false

            val mask = if (prefixLen == 0) 0L else (-1L shl (32 - prefixLen)) and 0xFFFFFFFFL
            (ipLong and mask) == (subnetLong and mask)
        } catch (_: Exception) {
            false
        }
    }

    private fun ipv4ToLong(ip: String): Long? {
        val parts = ip.split(".")
        if (parts.size != 4) return null
        var result = 0L
        for (part in parts) {
            val num = part.toIntOrNull() ?: return null
            if (num !in 0..255) return null
            result = (result shl 8) or num.toLong()
        }
        return result and 0xFFFFFFFFL
    }
}
