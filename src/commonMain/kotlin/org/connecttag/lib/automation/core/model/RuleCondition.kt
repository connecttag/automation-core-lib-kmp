package org.connecttag.lib.automation.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.MatchStrategy

/**
 * Predicate condition governing the execution of automation tasks.
 */
@Serializable
public sealed class RuleCondition {

    @Serializable
    @SerialName("TimeRange")
    public data class TimeRange(
        public val startTime: String,
        public val endTime: String
    ) : RuleCondition()

    @Serializable
    @SerialName("WeekDays")
    public data class WeekDays(
        public val days: List<Int>
    ) : RuleCondition()

    @Serializable
    @SerialName("BatteryLevel")
    public data class BatteryLevel(
        public val min: Int,
        public val max: Int,
        public val isCharging: Boolean? = null
    ) : RuleCondition()

    @Serializable
    @SerialName("WifiSSID")
    public data class WifiSSID(
        public val ssids: List<String>,
        public val mode: MatchMode = MatchMode.WHITELIST
    ) : RuleCondition()

    @Serializable
    @SerialName("ScreenState")
    public data class ScreenState(
        public val screenOn: Boolean
    ) : RuleCondition()

    @Serializable
    @SerialName("Location")
    public data class Location(
        public val lat: Double,
        public val lon: Double,
        public val radius: Float
    ) : RuleCondition()

    @Serializable
    @SerialName("NetworkType")
    public data class NetworkType(
        public val type: NetworkTypeValue
    ) : RuleCondition()

    @Serializable
    @SerialName("Notification")
    public data class Notification(
        public val appPackage: String,
        public val keyword: String,
        public val matchStrategy: MatchStrategy = MatchStrategy.CONTAINS
    ) : RuleCondition()

    @Serializable
    @SerialName("SMS")
    public data class SMS(
        public val sender: String,
        public val content: String,
        public val matchStrategy: MatchStrategy = MatchStrategy.CONTAINS
    ) : RuleCondition()

    @Serializable
    @SerialName("Geofence")
    public data class Geofence(
        public val lat: Double,
        public val lon: Double,
        public val radius: Float
    ) : RuleCondition()

    @Serializable
    @SerialName("Custom")
    public data class Custom(
        public val key: String,
        public val value: String,
        public val operator: ComparisonOperator
    ) : RuleCondition()

    @Serializable
    public enum class MatchMode { WHITELIST, BLACKLIST }

    @Serializable
    public enum class NetworkTypeValue { WIFI, CELLULAR, BLUETOOTH, ETHERNET }

    @Serializable
    public enum class ComparisonOperator { EQ, NEQ, GT, LT, GTE, LTE, CONTAINS, STARTS_WITH, ENDS_WITH }
}
