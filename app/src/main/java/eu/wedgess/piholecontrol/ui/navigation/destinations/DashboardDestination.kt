package eu.wedgess.piholecontrol.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.dashboard.view.DashboardScreen
import eu.wedgess.piholecontrol.ui.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.piholecontrol.ui.navigation.Screens

fun NavGraphBuilder.DashboardDestination(onComposing: (AppBarState) -> Unit) {
    composable(
        route = Screens.Dashboard.route
    ) {
        val viewModel: DashboardViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(AppBarState(showSearchView = false, showNavigateBackIcon = false))
        }

        DashboardScreen(uiResult)
    }
}