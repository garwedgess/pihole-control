package eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.view.QueryTypesScreen
import eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.viewmodel.QueryTypesViewModel

@Composable
fun QueryTypesRoot(viewModel: QueryTypesViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    QueryTypesScreen(uiResult, viewModel::onEvent)
}
