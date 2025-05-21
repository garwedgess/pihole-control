package eu.wedgess.piholecontrol.domain.model

data class LogFilterSuggestionsEntity(
    val domains: List<String>,
    val clientIpAddresses: List<String>,
    val clientNames: List<String>,
    val upstreams: List<String>,
    val queryTypes: List<String>,
    val statuses: List<String>,
    val replyTypes: List<String>,
    val dnsSecs: List<String>
)
