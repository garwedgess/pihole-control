package eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.UpstreamDestinationsContract

@Composable
fun UpstreamDestinationsScreen(
    statistics: UIResult<UpstreamDestinationsContract.UiState>,
    onEvent: (UpstreamDestinationsContract.Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statistics.Compose(
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
            onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
            onLoaded = { state ->
                UpstreamDestinationsContent(
                    chartDataCollection = state.donutChartDataCollection,
                    legendData = state.legendData,
                    onSelectedIndex = {
                        onEvent(UpstreamDestinationsContract.Event.OnLegendItemSelected(it))
                    }
                )
            }
        )
    }
}
