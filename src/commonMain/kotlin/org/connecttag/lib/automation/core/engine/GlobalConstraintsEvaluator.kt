package org.connecttag.lib.automation.core.engine

import org.connecttag.lib.automation.core.model.AutomationConfig
import org.connecttag.lib.automation.core.model.ValidationResult

/**
 * Snapshot of device environmental state needed for constraint checks.
 */
public data class DeviceEnvironmentSnapshot(
    val isWifiConnected: Boolean,
    val isCharging: Boolean,
    val batteryLevel: Int,
    val isScreenOn: Boolean
)

/**
 * Pure evaluator for hardware and environment constraints.
 */
public class GlobalConstraintsEvaluator {

    /**
     * Validates whether current environmental state meets all requirements defined in [config].
     */
    public fun validate(config: AutomationConfig, snapshot: DeviceEnvironmentSnapshot): ValidationResult {
        val errors = mutableListOf<String>()

        if (config.requireWifi && !snapshot.isWifiConnected) {
            errors.add("WiFi connection required")
        }

        if (config.requireCharging && !snapshot.isCharging) {
            errors.add("Device must be charging")
        }

        if (config.minBatteryLevel > 0 && snapshot.batteryLevel < config.minBatteryLevel) {
            errors.add("Battery level (${snapshot.batteryLevel}%) below minimum (${config.minBatteryLevel}%)")
        }

        if (config.screenOnRequired && !snapshot.isScreenOn) {
            errors.add("Screen must be on")
        }

        return ValidationResult(errors.isEmpty(), errors)
    }

    public fun isAllowed(config: AutomationConfig, snapshot: DeviceEnvironmentSnapshot): Boolean {
        return validate(config, snapshot).isValid
    }
}
