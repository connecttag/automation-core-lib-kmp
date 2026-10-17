package org.connecttag.lib.automation.core

import org.connecttag.lib.automation.core.engine.NetworkRuleMatcher
import org.connecttag.lib.automation.core.engine.PacketFlow
import org.connecttag.lib.automation.core.enums.MatchStrategy
import org.connecttag.lib.automation.core.enums.MatchType
import org.connecttag.lib.automation.core.model.RuleMatch
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NetworkRuleMatcherTest {

    private val matcher = NetworkRuleMatcher()

    @Test
    fun matchesWildcardDomain() {
        val rule = RuleMatch.Single(
            value = "*.malicious.org",
            type = MatchType.DOMAIN,
            strategy = MatchStrategy.EXACT
        )

        val packet = PacketFlow(daddr = "1.2.3.4", dport = 443)
        assertTrue(matcher.matches(packet, "sub.malicious.org", rule))
        assertTrue(matcher.matches(packet, "malicious.org", rule))
        assertFalse(matcher.matches(packet, "legitimate.org", rule))
    }

    @Test
    fun matchesCidrSubnet() {
        val rule = RuleMatch.Single(
            value = "10.0.0.0/8",
            type = MatchType.CIDR,
            strategy = MatchStrategy.EXACT
        )

        val packet1 = PacketFlow(daddr = "10.254.1.20", dport = 80)
        val packet2 = PacketFlow(daddr = "192.168.1.1", dport = 80)

        assertTrue(matcher.matches(packet1, null, rule))
        assertFalse(matcher.matches(packet2, null, rule))
    }

    @Test
    fun matchesPortRange() {
        val rule = RuleMatch.Single(
            value = "8000-8080",
            type = MatchType.PORT_RANGE,
            strategy = MatchStrategy.EXACT
        )

        assertTrue(matcher.matches(PacketFlow(dport = 8000), null, rule))
        assertTrue(matcher.matches(PacketFlow(dport = 8042), null, rule))
        assertTrue(matcher.matches(PacketFlow(dport = 8080), null, rule))
        assertFalse(matcher.matches(PacketFlow(dport = 8081), null, rule))
    }

    @Test
    fun matchesProtocol() {
        val tcpRule = RuleMatch.Single(
            value = "TCP",
            type = MatchType.PROTOCOL,
            strategy = MatchStrategy.EXACT
        )

        assertTrue(matcher.matches(PacketFlow(protocol = 6), null, tcpRule))
        assertFalse(matcher.matches(PacketFlow(protocol = 17), null, tcpRule))
    }
}
