package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.ExecutionStatus
import org.connecttag.lib.automation.core.enums.SchedulingMechanism
import org.connecttag.lib.automation.core.enums.SchedulingType
import org.connecttag.lib.automation.core.enums.TargetType
import org.connecttag.lib.automation.core.enums.TriggerEvent

/**
 * Domain model representing an end-to-end automation configuration rule.
 */
@Serializable
public data class AutomationSettings(
    public val id: Long = 0L,
    public val name: String = "",
    public val description: String = "",
    public val priority: Int = 0,
    public val tags: List<String> = emptyList(),
    public val targetType: TargetType = TargetType.APPLICATION,
    public val targetValue: String,
    public val version: Int = 1,
    public val isActive: Boolean = true,
    public val steps: List<AutomationStep> = emptyList(),

    public val createdAt: Long = 0L,
    public val updatedAt: Long = 0L,
    public val config: AutomationConfig = AutomationConfig(),
    public val conditions: List<RuleCondition> = emptyList(),
    public val matchRules: List<RuleMatch> = emptyList(),
    public val exclusionRules: List<RuleMatch> = emptyList(),
    public val automationSteps: List<AutomationStep> = emptyList(),
    public val schedulingConfig: SchedulingConfig = SchedulingConfig.Interval(15, SchedulingMechanism.WORKER),
    public val actionConfig: ActionConfig = ActionConfig.Log(ActionConfig.LogLevel.INFO, "Automation executed"),
    public val lastExecutionTime: Long = 0L,
    public val lastExecutionStatus: ExecutionStatus = ExecutionStatus.NONE,
    public val executionCount: Int = 0,
    public val lastExecutionMessage: String = "",
    public val isPendingExecution: Boolean = false,
    public val nextExecutionTime: Long = 0L,
    public val triggerEvent: TriggerEvent = TriggerEvent.APP_LAUNCHED,
    public val schedulingTool: SchedulingMechanism = SchedulingMechanism.WORKER,
    public val schedulingType: SchedulingType = SchedulingType.INTERVAL,
    public val configJson: String = "{}",
    public val conditionsJson: String = "[]",
    public val matchRulesJson: String = "[]",
    public val exclusionRulesJson: String = "[]",
    public val automationStepsJson: String = "[]",
    public val schedulingConfigJson: String = "[]",
    public val actionConfigJson: String = "{}"
) {
    /**
     * Determines whether this automation should be triggered at [currentTime].
     */
    public fun shouldExecute(currentTime: Long): Boolean {
        if (!isActive) return false
        if (config.maxExecutionCount > 0 && executionCount >= config.maxExecutionCount) return false

        return when (val sch = schedulingConfig) {
            is SchedulingConfig.Interval -> {
                val lastExecution = lastExecutionTime
                if (lastExecution == 0L) true
                else currentTime >= lastExecution + (sch.intervalMinutes * 60_000L)
            }
            is SchedulingConfig.OneTime -> {
                currentTime >= sch.executionTime && lastExecutionTime < sch.executionTime
            }
            is SchedulingConfig.Daily -> true
        }
    }

    /**
     * Checks if this rule needs to be rescheduled on device boot.
     */
    public fun requiresBootReschedule(): Boolean {
        return isActive && schedulingConfig.mechanism.requiresBootReschedule()
    }
}
