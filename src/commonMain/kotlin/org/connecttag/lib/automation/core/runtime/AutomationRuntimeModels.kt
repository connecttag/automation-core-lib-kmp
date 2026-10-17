package org.connecttag.lib.automation.core.runtime

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.TriggerEvent
import org.connecttag.lib.automation.core.model.AutomationSettings

/**
 * Trigger source initiating an automation decision or execution pipeline.
 */
@Serializable
public sealed class AutomationTriggerSource {
    @Serializable
    public data object WORKER : AutomationTriggerSource()

    @Serializable
    public data object ALARM : AutomationTriggerSource()

    @Serializable
    public data object FOREGROUND_SERVICE : AutomationTriggerSource()

    @Serializable
    public data object MANUAL : AutomationTriggerSource()

    @Serializable
    public data class Event(public val event: TriggerEvent) : AutomationTriggerSource()
}

/**
 * Evaluated decision describing whether an automation should proceed.
 */
@Serializable
public data class AutomationDecision(
    public val settingId: Long,
    public val targetValue: String,
    public val source: AutomationTriggerSource,
    public val shouldRun: Boolean,
    public val nextRunAt: Long,
    public val targetPackages: List<String>,
    public val reason: String
)

/**
 * Complete request to dispatch an automation execution run.
 */
@Serializable
public data class AutomationExecutionRequest(
    public val settingId: Long,
    public val source: AutomationTriggerSource,
    public val decision: AutomationDecision,
    public val traceId: String
)

/**
 * Result outcome of an executed automation run.
 */
@Serializable
public data class AutomationExecutionResult(
    public val packageName: String,
    public val source: AutomationTriggerSource,
    public val executed: Boolean,
    public val launchedAny: Boolean,
    public val reason: String,
    public val traceId: String
)
