package org.connecttag.lib.automation.core.runtime

import org.connecttag.lib.automation.core.enums.ExecutionStatus

/**
 * Storage contract for updating the execution lifecycle state of an automation rule.
 */
public interface AutomationExecutionStateStore {
    public suspend fun updateExecutionStatus(id: Long, status: ExecutionStatus, message: String)
    public suspend fun updatePendingExecution(id: Long, isPending: Boolean)
    public suspend fun recordSuccessfulExecution(id: Long, time: Long)
}

/**
 * Applies a successful state transition to an automation rule.
 */
public suspend fun applySuccessTransition(
    store: AutomationExecutionStateStore,
    id: Long,
    now: Long = System.currentTimeMillis()
) {
    store.recordSuccessfulExecution(id, now)
}

/**
 * Applies a failure state transition to an automation rule.
 */
public suspend fun applyFailureTransition(
    store: AutomationExecutionStateStore,
    id: Long,
    message: String,
    clearPending: Boolean = true
) {
    store.updateExecutionStatus(id, ExecutionStatus.FAILURE, message)
    if (clearPending) {
        store.updatePendingExecution(id, false)
    }
}
