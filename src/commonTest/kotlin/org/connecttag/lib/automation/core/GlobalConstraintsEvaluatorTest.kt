package org.connecttag.lib.automation.core

import org.connecttag.lib.automation.core.engine.DeviceEnvironmentSnapshot
import org.connecttag.lib.automation.core.engine.GlobalConstraintsEvaluator
import org.connecttag.lib.automation.core.model.AutomationConfig
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GlobalConstraintsEvaluatorTest {

    private val evaluator = GlobalConstraintsEvaluator()

    @Test
    fun validatesWifiConstraint() {
        val config = AutomationConfig(requireWifi = true)

        val wifiConnected = DeviceEnvironmentSnapshot(
            isWifiConnected = true,
            isCharging = false,
            batteryLevel = 80,
            isScreenOn = true
        )
        val wifiDisconnected = DeviceEnvironmentSnapshot(
            isWifiConnected = false,
            isCharging = false,
            batteryLevel = 80,
            isScreenOn = true
        )

        assertTrue(evaluator.isAllowed(config, wifiConnected))
        assertFalse(evaluator.isAllowed(config, wifiDisconnected))
    }

    @Test
    fun validatesBatteryAndChargingConstraints() {
        val config = AutomationConfig(requireCharging = true, minBatteryLevel = 25)

        val validSnapshot = DeviceEnvironmentSnapshot(
            isWifiConnected = true,
            isCharging = true,
            batteryLevel = 50,
            isScreenOn = true
        )
        val lowBatterySnapshot = DeviceEnvironmentSnapshot(
            isWifiConnected = true,
            isCharging = true,
            batteryLevel = 20,
            isScreenOn = true
        )
        val notChargingSnapshot = DeviceEnvironmentSnapshot(
            isWifiConnected = true,
            isCharging = false,
            batteryLevel = 50,
            isScreenOn = true
        )

        assertTrue(evaluator.isAllowed(config, validSnapshot))
        assertFalse(evaluator.isAllowed(config, lowBatterySnapshot))
        assertFalse(evaluator.isAllowed(config, notChargingSnapshot))
    }
}
