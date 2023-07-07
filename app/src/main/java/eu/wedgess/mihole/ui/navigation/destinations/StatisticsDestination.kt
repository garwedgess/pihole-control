package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.statistics.StatisticsContract
import eu.wedgess.mihole.ui.statistics.view.StatisticsScreen
import eu.wedgess.mihole.ui.statistics.viewmodel.StatisticsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.StatisticsDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(Screens.Statistics.route) {
        val viewModel: StatisticsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = "Statistics"
                )
            )
            do {
                viewModel.onEvent(StatisticsContract.Event.FetchStatistics)
                delay(10_000)
            } while (true)
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->

            }
        }

        StatisticsScreen(
            uiState,
            viewModel::onEvent
        )
    }
}