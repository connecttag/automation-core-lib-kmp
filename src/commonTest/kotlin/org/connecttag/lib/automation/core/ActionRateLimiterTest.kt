package org.connecttag.lib.automation.core

import org.connecttag.lib.automation.core.engine.ActionRateLimiter
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionRateLimiterTest {

    @Test
    fun throttlesRepeatedExecutions() {
        val limiter = ActionRateLimiter()
        val ruleId = 42L
        val target = "com.suspicious.app"
        val cooldownSeconds = 60
        val baseTime = 1000000L

        // First attempt allowed
        assertTrue(limiter.shouldAllowExecution(ruleId, target, cooldownSeconds, currentTime = baseTime))

        // Subsequent attempt within 60s blocked
        assertFalse(limiter.shouldAllowExecution(ruleId, target, cooldownSeconds, currentTime = baseTime + 30_000L))

        // Subsequent attempt after 60s allowed
        assertTrue(limiter.shouldAllowExecution(ruleId, target, cooldownSeconds, currentTime = baseTime + 61_000L))
    }

    @Test
    fun allowsImmediateExecutionWhenCooldownZero() {
        val limiter = ActionRateLimiter()
        val ruleId = 10L
        val target = "com.any.app"

        assertTrue(limiter.shouldAllowExecution(ruleId, target, 0, currentTime = 1000L))
        assertTrue(limiter.shouldAllowExecution(ruleId, target, 0, currentTime = 1001L))
    }
}
