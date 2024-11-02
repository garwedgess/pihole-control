package eu.wedgess.piholecontrol.ui.dashboard.model

import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.ui.compose.ResultType
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.dashboard.DashboardContract
import eu.wedgess.piholecontrol.utils.UiText
import timber.log.Timber

data class DashboardInfo(
    val summaryResult: Result<PiHoleSummary>,
    val queriesOverTimeResult: Result<PiHoleOverTimeData>,
    val clientQueriesOverTimeResult: Result<PiHoleClientsOverTimeData>
) {
    fun toUiResult(): UIResult<DashboardContract.UiState> {
        return when {
            summaryResult.isFailure &&
                    queriesOverTimeResult.isFailure &&
                    clientQueriesOverTimeResult.isFailure -> {
                UIResult.Error(
                    ResultType.Error.WithTitleAndSubTitle(
                        title = UiText.DynamicString("Failed to fetch dashboard info"),
                        subTitle = UiText.DynamicString(summaryResult.exceptionOrNull()?.message ?: "Unknown error")
                    )
                )
            }

            else -> {
                val summary = summaryResult.onFailure {
                    Timber.e("Failed to fetch summary: ${it.message}", it)
                }.getOrNull()
                val queriesOverTime = queriesOverTimeResult.onFailure {
                    Timber.e("Failed to fetch queries over time: ${it.message}", it)
                }.getOrNull()
                val clientQueriesOverTime = clientQueriesOverTimeResult.onFailure {
                    Timber.e("Failed to fetch client queries over time: ${it.message}", it)
                }.getOrNull()
                UIResult.Loaded(
                    DashboardContract.UiState(
                        summary, queriesOverTime, clientQueriesOverTime
                    )
                )
            }
        }
    }
}
