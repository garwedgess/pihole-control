package eu.wedgess.piholecontrol.presentation.dashboard.model

import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.utils.UiText
import timber.log.Timber

data class DashboardInfo(
    val summaryResult: Result<SummaryEntity>,
    val queriesOverTimeResult: Result<QueriesOverTimeEntity>,
    val clientQueriesOverTimeResult: Result<List<ClientOverTimeEntity>>
) {

    fun toUiResult(): UIResult<DashboardContract.UiState> {
        return when {
            summaryResult.isFailure &&
                    queriesOverTimeResult.isFailure &&
                    clientQueriesOverTimeResult.isFailure -> {
                handleErrorThrowable(summaryResult.exceptionOrNull())
            }

            else -> {
                val summary = summaryResult.onFailure {
                    Timber.e(it, "Failed to fetch summary: ${it.message}")
                }.getOrNull()
                val queriesOverTime = queriesOverTimeResult.onFailure {
                    Timber.e(it, "Failed to fetch queries over time: ${it.message}")
                }.getOrNull()?.run {
                    OverTimeLineChartInfo(
                        permittedChartInfo = LineChartInfo.PermittedQueriesOverLineChart(this.permitted),
                        blockedChartInfo = LineChartInfo.BlockedQueriesOverLineChart(this.blocked)
                    )
                }
                val clientQueriesOverTime = clientQueriesOverTimeResult.onFailure {
                    Timber.e(it, "Failed to fetch client queries over time: ${it.message}")
                }.getOrNull()?.run {
                    map {
                        LineChartInfo.ClientsOverLineChart(it)
                    }
                }
                UIResult.Loaded(
                    DashboardContract.UiState(
                        summary = summary,
                        overtimeLineChart = queriesOverTime,
                        clientQueriesOverTime = clientQueriesOverTime
                    )
                )
            }
        }
    }

    companion object {
        fun handleErrorThrowable(throwable: Throwable?): UIResult.Error {
            return UIResult.Error(
                ResultType.Error.WithTitleAndSubTitle(
                    title = UiText.DynamicString("Failed to fetch dashboard info"),
                    subTitle = UiText.DynamicString(
                        throwable?.message ?: "Unknown error"
                    )
                )
            )
        }
    }
}
