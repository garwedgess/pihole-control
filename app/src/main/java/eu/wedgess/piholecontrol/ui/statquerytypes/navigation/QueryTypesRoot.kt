package eu.wedgess.piholecontrol.ui.statquerytypes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.wedgess.piholecontrol.ui.statquerytypes.view.QueryTypesScreen
import eu.wedgess.piholecontrol.ui.statquerytypes.viewmodel.QueryTypesViewModel

@Composable
fun QueryTypesRoot(viewModel: QueryTypesViewModel = hiltViewModel()) {
    val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

    QueryTypesScreen(uiResult)
}