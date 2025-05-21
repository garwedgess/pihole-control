package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLogSuggestionsResponseData(
    val suggestions: PiHoleSuggestionData
) {
    @Serializable
    data class PiHoleSuggestionData(
        @SerialName("domain")
        val domains: List<String>,
        @SerialName("client_ip")
        val clientIpAddresses: List<String>,
        @SerialName("client_name")
        val clientNames: List<String>,
        @SerialName("upstream")
        val upstreams: List<String>,
        @SerialName("type")
        val queryTypes: List<String>,
        @SerialName("status")
        val statuses: List<String>,
        @SerialName("reply")
        val replyTypes: List<String>,
        @SerialName("dnssec")
        val dnsSecs: List<String>
    )
}
