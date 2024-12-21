package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.view.TopClientsContent
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.viewmodel.TopClientsStatsViewModel

@Composable
fun TopClientsScreenRoot(viewModel: TopClientsStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    uiResult.Compose(
        onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
        onLoaded = { TopClientsContent(it) },
        onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) }
    )
}
