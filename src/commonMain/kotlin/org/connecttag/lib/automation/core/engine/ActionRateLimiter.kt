package org.connecttag.lib.automation.core.engine

import java.util.concurrent.ConcurrentHashMap

/**
 * Thread-safe in-memory rate limiter / throttler for security and automation actions.
 *
 * Prevents alert fatigue and CPU/battery drainage when repeated network packets or events occur.
 */
public class ActionRateLimiter {

    // Key format: "$ruleId:$targetKey" -> Timestamp in milliseconds
    private val lastTriggerMap = ConcurrentHashMap<String, Long>()

    /**
     * Checks whether execution is allowed under the specified [cooldownSeconds].
     * If allowed, atomically records the current timestamp and returns true.
     * If within cooldown, returns false.
     */
    public fun shouldAllowExecution(
        ruleId: Long,
        targetKey: String,
        cooldownSeconds: Int,
        currentTime: Long = System.currentTimeMillis()
    ): Boolean {
        if (cooldownSeconds <= 0) return true

        val key = "$ruleId:$targetKey"
        val cooldownMillis = cooldownSeconds * 1000L

        var allowed = false
        lastTriggerMap.compute(key) { _, lastTime ->
            if (lastTime == null || (currentTime - lastTime) >= cooldownMillis) {
                allowed = true
                currentTime
            } else {
                allowed = false
                lastTime
            }
        }

        // Periodically purge entries older than 1 hour to keep memory footprint minimal
        if (lastTriggerMap.size > 200) {
            cleanupStaleEntries(currentTime)
        }

        return allowed
    }

    private fun cleanupStaleEntries(now: Long) {
        val oneHourAgo = now - 3600_000L
        lastTriggerMap.entries.removeIf { it.value < oneHourAgo }
    }

    /**
     * Resets throttling state (useful for tests or rule edits).
     */
    public fun reset() {
        lastTriggerMap.clear()
    }

    /**
     * Removes throttling state for a specific rule and target.
     */
    public fun reset(ruleId: Long, targetKey: String) {
        lastTriggerMap.remove("$ruleId:$targetKey")
    }

    /**
     * Current size of tracked cooldown keys.
     */
    public val size: Int
        get() = lastTriggerMap.size
}
