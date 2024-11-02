package eu.wedgess.piholecontrol.ui.stattopdomains.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.ui.stattopdomains.view.TopDomainsScreen
import eu.wedgess.piholecontrol.ui.stattopdomains.viewmodel.TopDomainsStatsViewModel

@Composable
fun TopDomainsRoot(viewModel: TopDomainsStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    TopDomainsScreen(uiResult)
}