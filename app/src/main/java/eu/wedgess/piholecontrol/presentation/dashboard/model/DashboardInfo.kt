package eu.wedgess.piholecontrol.presentation.dashboard.model

import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
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

    fun toUiResult(onRetry: () -> Unit): UIResult<DashboardContract.UiState> {
        return when {
            summaryResult.isFailure &&
                    queriesOverTimeResult.isFailure &&
                    clientQueriesOverTimeResult.isFailure -> {
                handleErrorThrowable(
                    throwable = summaryResult.exceptionOrNull(),
                    onRetry = onRetry
                )
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
        fun handleErrorThrowable(throwable: Throwable?, onRetry: () -> Unit): UIResult.Error {
            val message = if (throwable?.cause is ApiErrorThrowable) {
                when (val error = (throwable.cause as ApiErrorThrowable).apiErrorResponse) {
                    is ApiErrorResponse.V5 -> error.errorMessage
                    is ApiErrorResponse.V6 -> error.serverError.error?.message
                }
            } else {
                throwable?.message
            } ?: "Unknown error"
            return UIResult.Error(
                ResultType.Error.WithTitleAndSubTitleAndRetry(
                    title = UiText.DynamicString("Failed to fetch dashboard info"),
                    subTitle = UiText.DynamicString(message),
                    onRetry = onRetry
                )
            )
        }
    }
}
