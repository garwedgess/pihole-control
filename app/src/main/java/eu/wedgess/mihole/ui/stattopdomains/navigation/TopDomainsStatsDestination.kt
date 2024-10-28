package eu.wedgess.mihole.ui.stattopdomains.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.mihole.ui.stattopdomains.view.TopDomainsScreen
import eu.wedgess.mihole.ui.stattopdomains.viewmodel.TopDomainsStatsViewModel

@Composable
fun TopDomainsRoot(viewModel: TopDomainsStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    TopDomainsScreen(uiResult)
}