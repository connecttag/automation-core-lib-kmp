package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.TriggerType

/**
 * Trigger rule specification for automated launching.
 */
@Serializable
public data class AutomationTriggerConfig(
    public val type: TriggerType = TriggerType.MANUAL,
    public val enabled: Boolean = true,
    public val smsSenderFilter: String? = null,
    public val smsContentRegex: String? = null,
    public val wifiSsid: String? = null,
    public val batteryThreshold: Int? = null,
    public val batteryGoesBelow: Boolean = true,
    public val targetPackageName: String? = null,
    public val extraConfig: Map<String, String> = emptyMap()
)
