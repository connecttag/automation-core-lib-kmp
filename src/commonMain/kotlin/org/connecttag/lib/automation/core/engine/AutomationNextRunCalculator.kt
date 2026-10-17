package org.connecttag.lib.automation.core.engine

import org.connecttag.lib.automation.core.model.AutomationSettings
import org.connecttag.lib.automation.core.model.SchedulingConfig
import java.util.Calendar

/**
 * Pure calculator for predicting the next execution timestamp of scheduled automations.
 */
public class AutomationNextRunCalculator {

    public fun calculateNextRun(domain: AutomationSettings, now: Long = System.currentTimeMillis()): Long {
        return when (val sch = domain.schedulingConfig) {
            is SchedulingConfig.Interval -> {
                val last = domain.lastExecutionTime
                if (last == 0L) now
                else last + (sch.intervalMinutes * 60_000L)
            }
            is SchedulingConfig.OneTime -> {
                sch.executionTime
            }
            is SchedulingConfig.Daily -> {
                calculateDailyNextRun(sch.timeOfDay, now)
            }
        }
    }

    public fun calculateDailyNextRun(timeOfDay: String, now: Long = System.currentTimeMillis()): Long {
        return try {
            val parts = timeOfDay.split(":")
            if (parts.size != 2) return now + 86400000L
            val hour = parts[0].toInt()
            val min = parts[1].toInt()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = now
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, min)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            if (calendar.timeInMillis <= now) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            calendar.timeInMillis
        } catch (_: Exception) {
            now + 86400000L
        }
    }
}
