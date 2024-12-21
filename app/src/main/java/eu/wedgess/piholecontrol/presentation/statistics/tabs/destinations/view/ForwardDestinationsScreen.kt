package eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.ForwardDestinationsContract

@Composable
fun ForwardDestinationsScreen(
    statistics: UIResult<ForwardDestinationsContract.UiState>,
    onEvent: (ForwardDestinationsContract.Event) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statistics.Compose(
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
            onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
            onLoaded = { state ->
                ForwardDestinationsContent(
                    chartDataCollection = state.donutChartDataCollection,
                    legendData = state.legendData,
                    onSelectedIndex = {
                        onEvent(ForwardDestinationsContract.Event.OnLegendItemSelected(it))
                    }
                )
            }
        )
    }
}
