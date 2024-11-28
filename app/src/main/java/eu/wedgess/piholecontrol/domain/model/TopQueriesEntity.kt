package eu.wedgess.piholecontrol.domain.model

data class TopQueriesEntity(
    val allowed: List<TopDomainEntity>,
    val blocked: List<TopDomainEntity>
)
