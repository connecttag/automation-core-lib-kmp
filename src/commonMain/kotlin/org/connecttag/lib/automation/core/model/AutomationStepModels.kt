package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.AutomationStepType
import org.connecttag.lib.automation.core.enums.BindingType
import org.connecttag.lib.automation.core.enums.ConditionType
import org.connecttag.lib.automation.core.enums.FinderType
import org.connecttag.lib.automation.core.enums.RecoveryAction
import org.connecttag.lib.automation.core.enums.ValueType

/**
 * Anchor coordinate configuration for finding UI elements relative to an anchor node.
 */
@Serializable
public data class AnchorConfig(
    public val anchorType: FinderType = FinderType.TEXT,
    public val anchorKey: String = "",
    public val offsetX: Int = 0,
    public val offsetY: Int = 0,
    public val useRelativePercentage: Boolean = true
)

/**
 * Configuration for locating a target node on the screen.
 */
@Serializable
public data class FinderConfig(
    public val finderType: FinderType = FinderType.ID,
    public val finderKey: String = "",
    public val outputVariableName: String = "",
    public val valueType: ValueType = ValueType.STRING,
    public val useRegex: Boolean = false,
    public val anchorConfig: AnchorConfig? = null
)

/**
 * Failure recovery and retry policy for a single execution step.
 */
@Serializable
public data class RetryConfig(
    public val retryCount: Int = 0,
    public val retryIntervalMs: Long = 1000L,
    public val isOptional: Boolean = false,
    public val onFailure: RecoveryAction = RecoveryAction.NONE,
    public val useExponentialBackoff: Boolean = false,
    public val maxDelayMs: Long = 60000L
)

/**
 * Conditional guard for advancing to or executing a step.
 */
@Serializable
public data class StepCondition(
    public val type: ConditionType,
    public val value: String? = null,
    public val negate: Boolean = false,
    public val variableName: String? = null,
    public val expectedValue: String? = null
)

/**
 * Target application binding policy.
 */
@Serializable
public data class BindingConfig(
    public val type: BindingType = BindingType.APP_PACKAGE,
    public val references: List<String> = emptyList(),
    public val groupId: String = ""
)

/**
 * Catch-all handler for unexpected popups, dialogue prompts, or obstacles.
 */
@Serializable
public data class GlobalExceptionHandler(
    public val matchText: String = "",
    public val finderType: FinderType = FinderType.TEXT,
    public val action: AutomationStepType = AutomationStepType.CLICK,
    public val priority: Int = 0
)
