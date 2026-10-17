package org.connecttag.lib.automation.core.model

/**
 * Returns the recommended cooldown throttling duration in seconds for this action type.
 */
public fun ActionConfig.cooldownSeconds(): Int = when (this) {
    is ActionConfig.AudioAlert -> cooldownSeconds
    is ActionConfig.InteractiveNotification -> cooldownSeconds
    is ActionConfig.Telegram -> 30
    is ActionConfig.Api -> 15
    else -> 10
}
