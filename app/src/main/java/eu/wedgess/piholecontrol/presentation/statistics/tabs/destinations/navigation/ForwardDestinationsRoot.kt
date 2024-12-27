package eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.view.ForwardDestinationsScreen
import eu.wedgess.piholecontrol.presentation.statistics.tabs.destinations.viewmodel.ForwardDestinationsViewModel

@Composable
fun ForwardDestinationsRoot(viewModel: ForwardDestinationsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    ForwardDestinationsScreen(uiResult, viewModel::onEvent)
}
