package eu.wedgess.mihole.ui.dashboard.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.ErrorScreen
import eu.wedgess.mihole.ui.compose.LoadingScreen
import eu.wedgess.mihole.ui.compose.ResultType
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.ui.dashboard.view.components.ClientQueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.QueriesOverTimeSection
import eu.wedgess.mihole.ui.dashboard.view.components.SummarySection
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.ThemePreview

@Composable
fun DashboardScreen(uiResult: UIResult<DashboardContract.UiState>) {
    val scrollState = rememberScrollState()
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            uiResult.Compose(
                onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
                onLoaded = {
                    it.summary?.run { SummarySection(summary = this@run) }
                    it.queriesOverTime?.run { QueriesOverTimeSection(overTimeData = this@run) }
                    it.clientQueriesOverTime?.run {
                        ClientQueriesOverTimeSection(overTimeData = this@run)
                    }
                },
                onError = {
                    ErrorScreen(modifier = Modifier.fillMaxSize(), it)
                }
            )
        }
    }
}


@ThemePreview
@Composable
private fun DashboardScreenPreview() {
    MiHoleTheme {
        Surface {
            DashboardScreen(UIResult.Loading(ResultType.Loading.WithTitle()))
        }
    }
}