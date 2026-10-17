package org.connecttag.lib.automation.core.enums

import kotlinx.serialization.Serializable

/**
 * Execution filter condition for security actions.
 */
@Serializable
public enum class ActionConditionType(public val label: String) {
    ALL("الكل (بدون فلتر)"),
    SPECIFIC_DOMAIN("دومين محدد"),
    SPECIFIC_IP("عنوان IP محدد"),
    REGEX("تعابير نمطية (Regex)"),
    JS_EVAL("تعابير نمطية (Regex)"),
    NONE("");

    public companion object {
        public fun fromString(value: String): ActionConditionType = entries.find { it.name == value } ?: ALL
    }
}

/**
 * Trigger event defining when an action should be sent or evaluated.
 */
@Serializable
public enum class ActionSendWhen(public val label: String) {
    NEW_DOMAIN("عند اكتشاف دومين جديد"),
    CONNECTION_ALL("عند أي اتصال"),
    CONNECTION_SUCCESS("عند اتصال ناجح"),
    CONNECTION_FAIL("عند فشل الاتصال"),
    CONNECTION_BLOCKED("عند حظر اتصال"),
    HIGH_THREAT_BLOCKED("عند رصد تهديد أمني / قائمة سوداء"),
    GEO_BLOCKED("عند خرق الحظر الجغرافي"),
    NOTIFICATION("عند وصول إشعار محدد"),
    LOCATION_ENTER("عند دخول منطقة جغرافية"),
    LOCATION_EXIT("عند الخروج من منطقة جغرافية"),
    SMS_RECEIVED("عند وصول رسالة SMS"),
    APP_LAUNCHED("عند فتح تطبيق محدد"),
    SCREEN_ON("عند تشغيل الشاشة"),
    MANUAL("يدوي");

    public companion object {
        public fun fromString(value: String): ActionSendWhen = entries.find { it.name == value } ?: CONNECTION_ALL
    }
}

/**
 * Action type to execute upon trigger event satisfaction.
 */
@Serializable
public enum class ActionType(public val label: String) {
    TELEGRAM("إرسال إلى Telegram"),
    EMAIL("إرسال بريد إلكتروني"),
    REST_API("إرسال طلب API (JSON)"),
    LOG("حفظ في السجل فقط"),
    API("إرسال طلب API"),
    TOAST("عرض رسالة تنبيه (Toast)"),
    NOTIFICATION("إظهار إشعار"),
    AUDIO_ALERT("إنذار صوتي أمني واهتزاز"),
    AUTO_QUARANTINE("عزل التطبيق تلقائياً (حظر مؤقت)"),
    CUSTOM("إجراء مخصص");

    public companion object {
        public fun fromString(value: String): ActionType = entries.find { it.name == value } ?: LOG
    }
}

/**
 * Category of automation step.
 */
@Serializable
public enum class AutomationStepCategory(public val label: String) {
    INTERACTION("تفاعل (Interaction)"),
    NAVIGATION("تنقل (Navigation)"),
    LOGIC("منطق (Logic)"),
    SYSTEM("نظام (System)"),
    FLOW_CONTROL("تحكم في التدفق (Flow Control)"),
    STORAGE("تخزين (Storage)")
}

/**
 * Elementary action step in an automation sequence.
 */
@Serializable
public enum class AutomationStepType(
    public val label: String,
    public val category: AutomationStepCategory,
    public val description: String,
    public val iconKey: String,
    public val valueLabel: String? = null,
    public val requiresFinder: Boolean = false
) {
    CLICK("نقرة (Click)", AutomationStepCategory.INTERACTION, "النقر على عنصر محدد في الشاشة.", "click", requiresFinder = true),
    LONG_CLICK("نقرة مطولة (Long Click)", AutomationStepCategory.INTERACTION, "الضغط المستمر على عنصر في الشاشة.", "long_click", requiresFinder = true),
    SET_TEXT("إدخال نص (Set Text)", AutomationStepCategory.INTERACTION, "كتابة نص داخل حقل إدخال.", "keyboard", valueLabel = "النص المراد كتابته", requiresFinder = true),
    SWIPE("تمرير / سحب (Swipe)", AutomationStepCategory.INTERACTION, "محاكاة حركة السحب أو التمرير على الشاشة.", "swipe", valueLabel = "اتجاه السحب (UP, DOWN, LEFT, RIGHT)"),
    GESTURE("إيماءة مخصصة (Gesture)", AutomationStepCategory.INTERACTION, "رسم مسار لمسي مخصص.", "gesture", valueLabel = "إحداثيات المسار (X1,Y1 -> X2,Y2)"),
    SCROLL_FORWARD("تمرير للأمام", AutomationStepCategory.INTERACTION, "تمرير القائمة للأمام.", "swipe", requiresFinder = true),
    SCROLL_BACKWARD("تمرير للخلف", AutomationStepCategory.INTERACTION, "تمرير القائمة للخلف.", "swipe", requiresFinder = true),

    BACK("رجوع (Back)", AutomationStepCategory.NAVIGATION, "الضغط على زر الرجوع في النظام.", "back"),
    HOME("الرئيسية (Home)", AutomationStepCategory.NAVIGATION, "الرجوع للشاشة الرئيسية للنظام.", "home"),
    RECENTS("التطبيقات الحديثة (Recents)", AutomationStepCategory.NAVIGATION, "فتح شاشة التطبيقات المفتوحة مؤخراً.", "history"),
    OPEN_NOTIFICATIONS("فتح الإشعارات", AutomationStepCategory.NAVIGATION, "سحب لوحة الإشعارات العلوية.", "notifications"),
    OPEN_QUICK_SETTINGS("الإعدادات السريعة", AutomationStepCategory.NAVIGATION, "سحب لوحة الإعدادات السريعة.", "settings"),
    LAUNCH_APP("تشغيل تطبيق (Launch App)", AutomationStepCategory.NAVIGATION, "فتح تطبيق معين عبر اسم الحزمة.", "launch", valueLabel = "اسم الحزمة (Package Name)"),

    WAIT("انتظار (Wait / Sleep)", AutomationStepCategory.LOGIC, "تأخير زمني قبل الخطوة التالية.", "timer", valueLabel = "المدة بالمللي ثانية (ms)"),
    WAIT_FOR_ELEMENT("انتظار ظهور عنصر", AutomationStepCategory.LOGIC, "تعليق التنفيذ حتى يظهر عنصر في الشاشة.", "timer", valueLabel = "أقصى مدة انتظار (ms)", requiresFinder = true),
    CHECK_CONDITION("فحص شرط (If Condition)", AutomationStepCategory.LOGIC, "التحقق من حالة معينة قبل المتابعة.", "rule", valueLabel = "الشرط المطلوب"),
    EXTRACT_TEXT("استخراج نص (Extract)", AutomationStepCategory.LOGIC, "قراءة نص عنصر وحفظه كمتغير داخلي.", "data_exploration", valueLabel = "اسم المتغير (Variable Name)", requiresFinder = true),

    TAKE_SCREENSHOT("أخذ لقطة شاشة", AutomationStepCategory.SYSTEM, "التقاط صورة للشاشة الحالية وحفظها.", "screenshot"),
    SHOW_TOAST("عرض تنبيه (Toast)", AutomationStepCategory.SYSTEM, "إظهار رسالة سريعة منبثقة.", "notifications", valueLabel = "نص الرسالة"),
    COPY_TO_CLIPBOARD("نسخ للحافظة", AutomationStepCategory.SYSTEM, "نسخ نص معين إلى حافظة النظام.", "copy", valueLabel = "النص المراد نسخه"),
    EXECUTE_ACTION("تنفيذ إجراء أمني", AutomationStepCategory.SYSTEM, "إطلاق ActionConfig مسجل مسبقاً.", "play_arrow", valueLabel = "ActionConfig JSON"),
    SHELL_COMMAND("أمر نظام (Shell)", AutomationStepCategory.SYSTEM, "تنفيذ أمر Shell سريع.", "terminal", valueLabel = "الأمر المراد تنفيذه"),
    SYSTEM_KEY("زر نظام (Key Event)", AutomationStepCategory.SYSTEM, "محاكاة ضغط أزرار النظام.", "keyboard", valueLabel = "كود الزر (Key Code)"),

    SAVE_GLOBAL_VARIABLE("حفظ متغير عالمي", AutomationStepCategory.STORAGE, "حفظ قيمة في مخزن البيانات العالمي لمشاركته مع سيناريوهات أخرى.", "save", valueLabel = "اسم المتغير (Key)"),
    LOAD_GLOBAL_VARIABLE("تحميل متغير عالمي", AutomationStepCategory.STORAGE, "تحميل قيمة من مخزن البيانات العالمي.", "download", valueLabel = "اسم المتغير (Key)"),
    DELETE_GLOBAL_VARIABLE("حذف متغير عالمي", AutomationStepCategory.STORAGE, "حذف قيمة من مخزن البيانات العالمي.", "delete", valueLabel = "اسم المتغير (Key)");

    public companion object {
        public fun fromString(value: String): AutomationStepType = entries.find { it.name == value } ?: CLICK
    }
}

/**
 * Binding scope for target applications or services.
 */
@Serializable
public enum class BindingType(public val label: String) {
    APP_PACKAGE("تطبيق واحد"),
    PACKAGE_LIST("قائمة تطبيقات"),
    APP_GROUP("مجموعة تطبيقات"),
    GLOBAL("عام");

    public companion object {
        public fun fromString(value: String): BindingType = entries.find { it.name == value } ?: APP_PACKAGE
    }
}

/**
 * Condition evaluation predicate type for steps.
 */
@Serializable
public enum class ConditionType(public val label: String) {
    NONE("بدون شرط"),
    VIEW_EXISTS("إذا وجد معرف (ID)"),
    VIEW_TEXT_MATCHES("إذا وجد نص"),
    EXISTS("وجود العنصر"),
    NOT_EXISTS("عدم وجود العنصر"),
    TEXT_CONTAINS("يحتوي على نص"),
    TEXT_MATCHES("يطابق النص"),
    EQUALS("يساوي"),
    NOT_EQUALS("لا يساوي"),
    CONTAINS("يحتوي (عام)"),
    GREATER_THAN("أكبر من"),
    LESS_THAN("أصغر من");

    public companion object {
        public fun fromString(value: String): ConditionType = entries.find { it.name == value } ?: NONE
    }
}

/**
 * Execution status for automation runs.
 */
@Serializable
public enum class ExecutionStatus(public val label: String) {
    NONE("غير محدد"),
    SUCCESS("تم التنفيذ بنجاح"),
    FAILURE("فشل التنفيذ"),
    RUNNING("قيد التشغيل...");

    public companion object {
        public fun fromString(value: String): ExecutionStatus = entries.find { it.name == value } ?: NONE
    }
}

/**
 * UI element finding strategy.
 */
@Serializable
public enum class FinderType(public val label: String) {
    ID("ID العنصر"),
    TEXT("النص الكامل"),
    TEXT_CONTAINS("يحتوي على نص"),
    CONTENT_DESC("وصف المحتوى"),
    CLASS_NAME("اسم الكلاس"),
    XPATH("مسار XPath"),
    COORDINATES("الإحداثيات (X,Y)"),
    FUZZY_TEXT("نص تقريبي"),
    ANCHOR("المرساة (عنصر قريب)"),
    NONE("");

    public companion object {
        public fun fromString(value: String): FinderType = entries.find { it.name == value } ?: ID
    }
}

/**
 * Text matching strategy.
 */
@Serializable
public enum class MatchStrategy(public val label: String) {
    EXACT("مطابقة تامة"),
    CONTAINS("يحتوي على"),
    STARTS_WITH("يبدأ بـ"),
    ENDS_WITH("ينتهي بـ"),
    REGEX("تعبير نمطي (Regex)");

    public companion object {
        public fun fromString(value: String): MatchStrategy = entries.find { it.name == value } ?: CONTAINS
    }
}

/**
 * Type of network target to match.
 */
@Serializable
public enum class MatchType(public val label: String) {
    DOMAIN("دومين"),
    IP("عنوان IP"),
    KEYWORD("كلمة مفتاحية"),
    PORT("منفذ"),
    PORT_RANGE("نطاق منافذ"),
    PROTOCOL("بروتوكول"),
    CIDR("شبكة فرعية IP"),
    ALL("الكل");

    public companion object {
        public fun fromString(value: String): MatchType = entries.find { it.name == value } ?: DOMAIN
    }
}

/**
 * Strategy to recover when a step execution fails.
 */
@Serializable
public enum class RecoveryAction(public val label: String) {
    NONE("لا شيء"),
    RETRY("إعادة المحاولة"),
    EXPONENTIAL_RETRY("إعادة محاولة (Exponential)"),
    SCROLL_DOWN_AND_RETRY("تمرير للأسفل وإعادة المحاولة"),
    SCROLL_UP_AND_RETRY("تمرير للأعلى وإعادة المحاولة"),
    STOP("إيقاف الأتمتة")
}

/**
 * Match kind: inclusion vs exclusion.
 */
@Serializable
public enum class RuleMatchKind {
    MATCH,
    EXCLUSION
}

/**
 * Background scheduling mechanism.
 */
@Serializable
public enum class SchedulingMechanism(public val label: String) {
    WORKER("WorkManager (افتراضي)"),
    ALARM("AlarmManager (دقيق)"),
    SERVICE("خدمة مستمرة (Foreground Service)");

    public fun isWorkerBased(): Boolean = this == WORKER
    public fun isAlarmBased(): Boolean = this == ALARM
    public fun isServiceBased(): Boolean = this == SERVICE
    public fun requiresBootReschedule(): Boolean = this == ALARM || this == SERVICE

    public companion object {
        public fun fromString(value: String): SchedulingMechanism = entries.find { it.name == value } ?: WORKER
    }
}

/**
 * Frequency type of schedule.
 */
@Serializable
public enum class SchedulingType(public val label: String) {
    INTERVAL("دوري"),
    ONE_TIME("مرة واحدة"),
    DAILY("يومي/أسبوعي");

    public companion object {
        public fun fromString(value: String): SchedulingType = entries.find { it.name == value } ?: INTERVAL
    }
}

/**
 * Automation target entity type.
 */
@Serializable
public enum class TargetType(public val label: String) {
    APPLICATION("تطبيق"),
    COMPONENT("مكون"),
    DEVICE("الجهاز"),
    GLOBAL("عام"),
    SYSTEM("النظام");

    public companion object {
        public fun fromString(value: String): TargetType = entries.find { it.name == value } ?: APPLICATION
    }
}

/**
 * Test run status for real-time runner feedback.
 */
@Serializable
public sealed class TestRunStatus {
    @Serializable
    public data object Idle : TestRunStatus()
    @Serializable
    public data object Running : TestRunStatus()
    @Serializable
    public data class Success(public val message: String) : TestRunStatus()
    @Serializable
    public data class Error(public val message: String, public val stepIndex: Int? = null) : TestRunStatus()
}

/**
 * Runtime events that trigger automation flows.
 */
@Serializable
public enum class TriggerEvent(public val label: String) {
    NOTIFICATION("عند وصول إشعار محدد"),
    LOCATION_ENTER("عند دخول منطقة جغرافية"),
    LOCATION_EXIT("عند الخروج من منطقة جغرافية"),
    SMS_RECEIVED("عند وصول رسالة SMS"),
    APP_LAUNCHED("عند فتح تطبيق محدد"),
    APP_CLOSED("عند إغلاق تطبيق محدد"),
    SCREEN_ON("عند تشغيل الشاشة"),
    SCREEN_OFF("عند إطفاء الشاشة"),
    WIFI_CONNECTED("عند الاتصال بالواي فاي"),
    WIFI_DISCONNECTED("عند قطع الواي فاي"),
    BATTERY_LEVEL_CHANGED("عند تغير مستوى البطارية"),
    BATTERY_LOW("عند انخفاض البطارية"),
    BATTERY_OKAY("عند اكتمال شحن البطارية"),
    DEVICE_BOOT("عند إقلاع الجهاز"),
    TIME_TICK("تحديث زمني (دقيقة)"),
    MANUAL("يدوي");

    public companion object {
        public fun fromString(value: String): TriggerEvent = entries.find { it.name == value } ?: APP_LAUNCHED
    }
}

/**
 * Trigger selector for configuration templates.
 */
@Serializable
public enum class TriggerType {
    MANUAL,
    SCHEDULED,
    SMS_RECEIVED,
    WIFI_CONNECTED,
    WIFI_DISCONNECTED,
    BATTERY_LEVEL_CHANGED,
    APP_OPENED,
    DEVICE_BOOT;

    public fun toTriggerEvent(): TriggerEvent = when (this) {
        MANUAL -> TriggerEvent.MANUAL
        SCHEDULED -> TriggerEvent.TIME_TICK
        SMS_RECEIVED -> TriggerEvent.SMS_RECEIVED
        WIFI_CONNECTED -> TriggerEvent.WIFI_CONNECTED
        WIFI_DISCONNECTED -> TriggerEvent.WIFI_DISCONNECTED
        BATTERY_LEVEL_CHANGED -> TriggerEvent.BATTERY_LEVEL_CHANGED
        APP_OPENED -> TriggerEvent.APP_LAUNCHED
        DEVICE_BOOT -> TriggerEvent.DEVICE_BOOT
    }
}

/**
 * Dynamic value data type.
 */
@Serializable
public enum class ValueType(public val label: String) {
    STRING("نص (String)"),
    INT("رقم (Integer)"),
    BOOLEAN("منطقي (Boolean)"),
    FLOAT("رقم عشري (Float)");

    public companion object {
        public fun fromString(value: String): ValueType = entries.find { it.name == value } ?: STRING
    }
}
