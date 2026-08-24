package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.Serializable

@Serializable
data class PiHoleLocalDnsResponseData(
    val config: ConfigData
) {
    @Serializable
    data class ConfigData(
        val dns: DnsData
    )

    @Serializable
    data class DnsData(
        val hosts: List<String> = emptyList()
    )
}
