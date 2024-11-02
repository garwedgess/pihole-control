package eu.wedgess.piholecontrol.ui.navigation

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.navigation.destinations.DashboardDestination
import eu.wedgess.piholecontrol.ui.navigation.destinations.FiltersDestination
import eu.wedgess.piholecontrol.ui.navigation.destinations.LogsDestination
import eu.wedgess.piholecontrol.ui.navigation.destinations.StatisticsDestination

@OptIn(ExperimentalGetImage::class)
@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = Graphs.Main.route,
        startDestination = Screens.Dashboard.route,
        modifier = modifier
    ) {
        DashboardDestination(onComposing)
        StatisticsDestination(onComposing)
        FiltersDestination(onComposing)
        LogsDestination(onComposing)
        SettingsNavigationGraph(navController, onComposing)
    }
}