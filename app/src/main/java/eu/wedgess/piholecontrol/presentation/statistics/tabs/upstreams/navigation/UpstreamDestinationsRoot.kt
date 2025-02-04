package eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.view.UpstreamDestinationsScreen
import eu.wedgess.piholecontrol.presentation.statistics.tabs.upstreams.viewmodel.UpstreamDestinationsViewModel

@Composable
fun UpstreamDestinationsRoot(viewModel: UpstreamDestinationsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    UpstreamDestinationsScreen(uiResult, viewModel::onEvent)
}
