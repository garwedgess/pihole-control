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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.dashboard.model.OverTimeLineChartInfo
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.ClientQueriesOverTimeSection
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.QueriesOverTimeSection
import eu.wedgess.piholecontrol.presentation.dashboard.view.components.SummarySection
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.UiText

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
private fun DashboardScreenPreview(
    @PreviewParameter(DashboardScreenPreviewParam::class)
    uiResult: UIResult<DashboardContract.UiState>
) {
    PiHoleControlTheme {
        Surface {
            DashboardScreen(uiResult)
        }
    }
}

private class DashboardScreenPreviewParam :
    PreviewParameterProvider<UIResult<DashboardContract.UiState>> {
    override val values: Sequence<UIResult<DashboardContract.UiState>>
        get() = sequenceOf(
            UIResult.Error(
                ResultType.Error.WithTitleAndSubTitle(
                    title = UiText.DynamicString("Failed to fetch dashboard info"),
                    subTitle = UiText.DynamicString("HTTP error 404: Bad request")
                )
            ),
            UIResult.Loading(ResultType.Loading.WithTitle()),
            UIResult.Loaded(
                DashboardContract.UiState(
                    summary = SummaryEntity(
                        dnsQueries = 234565,
                        adsBlocked = 2456,
                        domainsBlocked = 1234567,
                        adsPercentage = 30f,
                        uniqueClients = 25
                    ),
                    overtimeLineChart = OverTimeLineChartInfo(
                        permittedChartInfo = LineChartInfo.PermittedQueriesOverLineChart(
                            entity = listOf()
                        ),
                        blockedChartInfo = LineChartInfo.BlockedQueriesOverLineChart(
                            entity = listOf()
                        )
                    ),
                    clientQueriesOverTime = listOf(
                        LineChartInfo.ClientsOverLineChart(
                            entity = ClientOverTimeEntity(
                                clientName = "Test",
                                clientIp = "192.168.61.21",
                                clientActivity = listOf(
                                    OverTimeEntity(timestamp = 1734367500, hits = 167),
                                    OverTimeEntity(timestamp = 1734368100, hits = 286),
                                    OverTimeEntity(timestamp = 1734368700, hits = 25),
                                    OverTimeEntity(timestamp = 1734369300, hits = 0),
                                    OverTimeEntity(timestamp = 1734369900, hits = 92),
                                    OverTimeEntity(timestamp = 1734370500, hits = 81)
                                )
                            )
                        ),
                        LineChartInfo.ClientsOverLineChart(
                            entity = ClientOverTimeEntity(
                                clientName = "Router",
                                clientIp = "192.168.61.1",
                                clientActivity = listOf(
                                    OverTimeEntity(timestamp = 1734367500, hits = 267),
                                    OverTimeEntity(timestamp = 1734368100, hits = 296),
                                    OverTimeEntity(timestamp = 1734368700, hits = 250),
                                    OverTimeEntity(timestamp = 1734369300, hits = 100),
                                    OverTimeEntity(timestamp = 1734369900, hits = 92),
                                    OverTimeEntity(timestamp = 1734370500, hits = 81)
                                )
                            )
                        )
                    )
                )
            )
        )
}
