package eu.wedgess.piholecontrol.presentation.dashboard.extensions

import eu.wedgess.piholecontrol.domain.model.DashboardInfoEntity
import eu.wedgess.piholecontrol.presentation.dashboard.model.DashboardInfo

fun DashboardInfoEntity.toUiInfo() = DashboardInfo(
    summaryResult = this.summaryResult,
    queriesOverTimeResult = this.queriesOverTimeResult,
    clientQueriesOverTimeResult = this.clientQueriesOverTimeResult
)
