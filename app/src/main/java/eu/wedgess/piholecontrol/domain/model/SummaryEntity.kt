package eu.wedgess.piholecontrol.domain.model

data class SummaryEntity(
    val dnsQueries: Int,
    val adsBlocked: Int,
    val domainsBlocked: Int,
    val adsPercentage: Float,
    val uniqueClients: Int
)