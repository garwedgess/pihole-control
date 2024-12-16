package eu.wedgess.piholecontrol.presentation.dashboard.view

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
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.ClientQueriesOverTimeSection
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.QueriesOverTimeSection
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.SummarySection
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.ThemePreview

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
                    it.overtimeLineChart?.run { QueriesOverTimeSection(overTimeData = this@run) }
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
    PiHoleControlTheme {
        Surface {
            DashboardScreen(UIResult.Loading(ResultType.Loading.WithTitle()))
        }
    }
}
