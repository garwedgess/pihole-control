package eu.wedgess.mihole.ui.dashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.ui.dashboard.view.components.ClientQueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.QueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.SummarySection
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun DashboardScreen(
    uiState: DashboardContract.UiState,
    onEvent: (DashboardContract.Event) -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
        ) {
            when (val summary = uiState.summary) {
                is UiResult.Success -> SummarySection(summary = summary.data)
                is UiResult.Error -> ErrorMessage(
                    errorMessage = summary.errorMessage.asString(),
                    onRetry = { onEvent(DashboardContract.Event.FetchSummary) })

                UiResult.Loading -> LoadingContent(
                    modifier = Modifier.heightIn(min = MiHoleTheme.dimens.size.homeLoadingHeight),
                    message = stringResource(R.string.home_fetching_statistics)
                )
            }

            when (val overtime = uiState.queriesOverTime) {
                is UiResult.Success -> QueriesOverTimeSection(
                    overTimeData = overtime.data
                )

                is UiResult.Error -> ErrorMessage(
                    errorMessage = overtime.errorMessage.asString(),
                    onRetry = { onEvent(DashboardContract.Event.FetchQueriesOvertime) })

                UiResult.Loading -> LoadingContent(
                    modifier = Modifier.heightIn(min = MiHoleTheme.dimens.size.homeLoadingHeight),
                    message = stringResource(R.string.home_fetching_over_time_data)
                )
            }

            when (val overtime = uiState.clientQueriesOverTime) {
                is UiResult.Success -> ClientQueriesOverTimeSection(
                    overTimeData = overtime.data
                )

                is UiResult.Error -> ErrorMessage(
                    errorMessage = overtime.errorMessage.asString(),
                    onRetry = { onEvent(DashboardContract.Event.FetchClientQueriesOvertime) })

                UiResult.Loading -> LoadingContent(
                    modifier = Modifier.heightIn(min = MiHoleTheme.dimens.size.homeLoadingHeight),
                    message = stringResource(R.string.home_fetching_clients_over_time_data)
                )
            }
        }
    }
}