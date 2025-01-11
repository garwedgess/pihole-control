package eu.wedgess.piholecontrol.domain.model

data class TopClientQueriesEntity(
    val all: List<TopClientEntity>,
    val blocked: List<TopClientEntity>
)
