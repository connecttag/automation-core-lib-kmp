package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.AutomationStepType

/**
 * Single executable step in an automation scenario.
 */
@Serializable
public data class AutomationStep(
    public val type: AutomationStepType,
    public val value: String,
    public val extra: Map<String, String> = emptyMap(),
    public val stepLabel: String = "",
    public val finderConfig: FinderConfig? = null,
    public val retryConfig: RetryConfig = RetryConfig(),
    public val timeoutMs: Long = 5000L,
    public val anchorConfig: AnchorConfig? = null,
    public val breadcrumbPath: String? = null,
    public val isOptional: Boolean = false,
    public val isActive: Boolean = true,
    public val condition: StepCondition? = null,
    public val bounds: String? = null
)
