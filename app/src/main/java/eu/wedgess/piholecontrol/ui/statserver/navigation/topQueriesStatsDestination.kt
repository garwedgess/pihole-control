package eu.wedgess.piholecontrol.ui.statserver.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.ui.statserver.view.ServersScreen
import eu.wedgess.piholecontrol.ui.statserver.viewmodel.ServerStatsViewModel

@Composable
fun ServersRoot(viewModel: ServerStatsViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    ServersScreen(uiResult)
}