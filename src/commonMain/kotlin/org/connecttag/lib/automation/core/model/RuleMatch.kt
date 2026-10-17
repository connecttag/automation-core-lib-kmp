package org.connecttag.lib.automation.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.connecttag.lib.automation.core.enums.MatchStrategy
import org.connecttag.lib.automation.core.enums.MatchType

/**
 * Filter match predicate for network traffic and domain flows.
 */
@Serializable
public sealed class RuleMatch {

    @Serializable
    @SerialName("Single")
    public data class Single(
        public val value: String,
        public val type: MatchType,
        public val strategy: MatchStrategy
    ) : RuleMatch()

    @Serializable
    @SerialName("Group")
    public data class Group(
        public val values: List<String>,
        public val type: MatchType,
        public val strategy: MatchStrategy
    ) : RuleMatch()

    @Serializable
    @SerialName("All")
    public data object All : RuleMatch()
}
