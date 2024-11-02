package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.dashboard.view.DashboardScreen
import eu.wedgess.mihole.ui.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.mihole.ui.navigation.Screens

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