package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable

/**
 * Top-level operational constraints and execution flags for an automation rule.
 */
@Serializable
public data class AutomationConfig(
    public val triggerConfig: AutomationTriggerConfig? = null,
    public val keepAlive: Boolean = false,
    public val openAtInterval: Boolean = false,
    public val openIntervalMinutes: Int = 15,
    public val requireWifi: Boolean = false,
    public val requireCharging: Boolean = false,
    public val minBatteryLevel: Int = 0,
    public val screenOnRequired: Boolean = false,
    public val maxExecutionCount: Int = 0,
    public val executeOnAppOpen: Boolean = false,
    public val bindingConfig: BindingConfig = BindingConfig(),
    public val customProperties: Map<String, String> = emptyMap(),
    public val stopOnFailure: Boolean = true,
    public val globalExceptionHandlers: List<GlobalExceptionHandler> = emptyList()
)
