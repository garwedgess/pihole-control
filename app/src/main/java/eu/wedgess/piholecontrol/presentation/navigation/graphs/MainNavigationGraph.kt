package eu.wedgess.piholecontrol.presentation.navigation.graphs

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.dashboard.navigation.dashboardRoot
import eu.wedgess.piholecontrol.presentation.filters.navigation.filtersRoot
import eu.wedgess.piholecontrol.presentation.logs.navigation.logsRoot
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.statistics.navigation.statisticsRoot

@OptIn(ExperimentalGetImage::class)
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard,
        modifier = modifier
    ) {
        dashboardRoot(onComposing)
        statisticsRoot(onComposing)
        filtersRoot(onComposing)
        logsRoot(onComposing)
        settingsNavigationGraph(navController, onComposing)
    }
}