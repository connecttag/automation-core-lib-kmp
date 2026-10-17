package org.connecttag.lib.automation.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.ActionConditionType
import org.connecttag.lib.automation.core.enums.ActionSendWhen

/**
 * Concrete action execution specification triggered by security and network events.
 */
@Serializable
public sealed class ActionConfig {
    public abstract val sendWhen: ActionSendWhen
    public abstract val conditionType: ActionConditionType
    public abstract val conditionValue: String

    @Serializable
    @SerialName("Telegram")
    public data class Telegram(
        public val chatId: String,
        public val token: String,
        public val message: String,
        public val parseMode: String = "HTML",
        public val useDefaultChatId: Boolean = false,
        public val useDefaultToken: Boolean = false,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("Email")
    public data class Email(
        public val to: List<String>,
        public val subject: String,
        public val body: String,
        public val cc: List<String> = emptyList(),
        public val bcc: List<String> = emptyList(),
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    public sealed class ApiAuthentication {
        @Serializable
        @SerialName("None")
        public data object None : ApiAuthentication()

        @Serializable
        @SerialName("Basic")
        public data class Basic(public val username: String, public val password: String) : ApiAuthentication()

        @Serializable
        @SerialName("Bearer")
        public data class Bearer(public val token: String) : ApiAuthentication()

        @Serializable
        @SerialName("ApiKey")
        public data class ApiKey(
            public val key: String,
            public val value: String,
            public val addTo: ApiKeyLocation = ApiKeyLocation.HEADER
        ) : ApiAuthentication()

        @Serializable
        public enum class ApiKeyLocation { HEADER, QUERY }
    }

    @Serializable
    @SerialName("Api")
    public data class Api(
        public val url: String,
        public val method: HttpMethod,
        public val headers: Map<String, String>,
        public val body: String,
        public val authentication: ApiAuthentication = ApiAuthentication.None,
        public val timeoutSeconds: Int = 30,
        public val isRest: Boolean = false,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("Toast")
    public data class Toast(
        public val message: String,
        public val durationShort: Boolean = true,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("Log")
    public data class Log(
        public val level: LogLevel,
        public val message: String,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("CustomAction")
    public data class Custom(
        public val type: String,
        public val parameters: Map<String, String>,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("InAppNotification")
    public data class InAppNotification(
        public val title: String,
        public val body: String,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_FAIL,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("AudioAlert")
    public data class AudioAlert(
        public val soundType: AlertSoundType = AlertSoundType.ALARM,
        public val vibrate: Boolean = true,
        public val cooldownSeconds: Int = 60,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_BLOCKED,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("InteractiveNotification")
    public data class InteractiveNotification(
        public val title: String = "",
        public val body: String = "",
        public val allowQuickQuarantine: Boolean = true,
        public val allowQuickAllow: Boolean = true,
        public val cooldownSeconds: Int = 60,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_BLOCKED,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    @SerialName("AutoQuarantine")
    public data class AutoQuarantine(
        public val durationMinutes: Int = 15,
        public val notifyUser: Boolean = true,
        override val sendWhen: ActionSendWhen = ActionSendWhen.CONNECTION_BLOCKED,
        override val conditionType: ActionConditionType = ActionConditionType.NONE,
        override val conditionValue: String = ""
    ) : ActionConfig()

    @Serializable
    public enum class AlertSoundType { ALARM, WARNING, NOTIFICATION, SUBTLE }

    @Serializable
    public enum class HttpMethod { GET, POST, PUT, DELETE, PATCH }

    @Serializable
    public enum class LogLevel { DEBUG, INFO, WARNING, ERROR }
}
