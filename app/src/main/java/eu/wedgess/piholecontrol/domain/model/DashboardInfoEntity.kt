package eu.wedgess.piholecontrol.domain.model

data class DashboardInfoEntity(
    val summaryResult: Result<SummaryEntity>,
    val queriesOverTimeResult: Result<QueriesOverTimeEntity>,
    val clientQueriesOverTimeResult: Result<List<ClientOverTimeEntity>>
)
