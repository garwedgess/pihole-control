package eu.wedgess.piholecontrol.presentation.dashboard.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.dashboard.view.DashboardScreen
import eu.wedgess.piholecontrol.presentation.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens

fun NavGraphBuilder.dashboardRoot(onComposing: (AppBarState) -> Unit) {
    composable<Screens.Dashboard> {
        val viewModel: DashboardViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(AppBarState(showSearchView = false, showNavigateBackIcon = false))
        }

        DashboardScreen(uiResult)
    }
}