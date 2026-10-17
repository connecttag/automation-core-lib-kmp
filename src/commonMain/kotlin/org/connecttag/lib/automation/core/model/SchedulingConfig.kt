package org.connecttag.lib.automation.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.SchedulingMechanism

/**
 * Schedule policy configuration defining when and how frequently an automation runs.
 */
@Serializable
public sealed class SchedulingConfig {
    public abstract val mechanism: SchedulingMechanism

    @Serializable
    @SerialName("Interval")
    public data class Interval(
        public val intervalMinutes: Int,
        override val mechanism: SchedulingMechanism = SchedulingMechanism.WORKER,
        public val flexTimeMinutes: Int = 0
    ) : SchedulingConfig()

    @Serializable
    @SerialName("OneTime")
    public data class OneTime(
        public val executionTime: Long,
        override val mechanism: SchedulingMechanism = SchedulingMechanism.WORKER
    ) : SchedulingConfig()

    @Serializable
    @SerialName("Daily")
    public data class Daily(
        public val timeOfDay: String, // Format: "HH:mm"
        public val daysOfWeek: List<Int> = emptyList(), // 1-7, Monday = 1
        override val mechanism: SchedulingMechanism = SchedulingMechanism.WORKER
    ) : SchedulingConfig()
}
