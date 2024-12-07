package eu.wedgess.piholecontrol.domain.model

data class QueriesOverTimeEntity(
    val permitted: List<OverTimeEntity>,
    val blocked: List<OverTimeEntity>
)