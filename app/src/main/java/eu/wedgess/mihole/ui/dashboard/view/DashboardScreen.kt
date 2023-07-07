package eu.wedgess.mihole.ui.dashboard.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.ui.dashboard.view.components.ClientQueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.QueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.SummarySection

@Composable
fun DashboardScreen(
    uiState: DashboardContract.UiState,
    onEvent: (DashboardContract.Event) -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            when (val summary = uiState.summary) {
                is Resource.Success -> SummarySection(summary = summary.data)
                is Resource.Error -> ErrorMessage(
                    errorMessage = summary.errorMessage,
                    onRetry = { onEvent(DashboardContract.Event.FetchSummary) })

                Resource.Loading -> LoadingContent(message = "Fetching statistics")
            }

            when (val overtime = uiState.queriesOverTime) {
                is Resource.Success -> QueriesOverTimeSection(overTimeData = overtime.data)
                is Resource.Error -> ErrorMessage(
                    errorMessage = overtime.errorMessage,
                    onRetry = { onEvent(DashboardContract.Event.FetchQueriesOvertime) })

                Resource.Loading -> LoadingContent(message = "Fetching over time data")
            }

            when (val overtime = uiState.clientQueriesOverTime) {
                is Resource.Success -> ClientQueriesOverTimeSection(overTimeData = overtime.data)
                is Resource.Error -> ErrorMessage(
                    errorMessage = overtime.errorMessage,
                    onRetry = { onEvent(DashboardContract.Event.FetchClientQueriesOvertime) })

                Resource.Loading -> LoadingContent(message = "Fetching clients over time data")
            }
        }
    }
}