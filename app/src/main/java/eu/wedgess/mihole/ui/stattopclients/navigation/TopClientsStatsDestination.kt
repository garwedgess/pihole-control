package eu.wedgess.mihole.ui.stattopclients.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.ErrorScreen
import eu.wedgess.mihole.ui.compose.LoadingScreen
import eu.wedgess.mihole.ui.stattopclients.view.TopClientsContent
import eu.wedgess.mihole.ui.stattopclients.viewmodel.TopClientsStatsViewModel

@Composable
fun TopClientsScreenRoot(viewModel: TopClientsStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    uiResult.Compose(
        onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
        onLoaded = { TopClientsContent(it.topSources) },
        onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) }
    )
}