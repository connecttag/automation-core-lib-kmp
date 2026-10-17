package org.connecttag.lib.automation.core.model

import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.AutomationStepType

/**
 * Result metrics for a single executed step.
 */
@Serializable
public data class StepExecutionResult(
    public val stepIndex: Int,
    public val stepType: AutomationStepType,
    public val stepLabel: String,
    public val success: Boolean,
    public val durationMs: Long,
    public val message: String? = null,
    public val errorMessage: String? = null,
    public val retryCount: Int = 0
)

/**
 * Complete forensic execution trace of an automation run.
 */
@Serializable
public data class ExecutionTrace(
    public val steps: List<StepExecutionResult>,
    public val variablesAtEnd: Map<String, String>,
    public val startTime: Long,
    public val endTime: Long,
    public val screenshotPath: String? = null
)

/**
 * Validation result for automation configuration checks.
 */
@Serializable
public data class ValidationResult(
    public val isValid: Boolean,
    public val errors: List<String> = emptyList()
)
