package eu.wedgess.mihole.ui.dashboard.model

import eu.wedgess.mihole.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleOverTimeData
import eu.wedgess.mihole.data.model.responses.PiHoleSummary
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.utils.UiText
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
