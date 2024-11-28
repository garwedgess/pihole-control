package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view.TopDomainsScreen
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.viewmodel.TopDomainsStatsViewModel

@Composable
fun TopDomainsRoot(viewModel: TopDomainsStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    TopDomainsScreen(uiResult)
}