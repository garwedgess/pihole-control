package eu.wedgess.mihole.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.navigation.destinations.DashboardDestination
import eu.wedgess.mihole.ui.navigation.destinations.FiltersDestination
import eu.wedgess.mihole.ui.navigation.destinations.LogsDestination
import eu.wedgess.mihole.ui.navigation.destinations.StatisticsDestination

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
        DashboardDestination()
        StatisticsDestination()
        FiltersDestination(onComposing)
        LogsDestination(onComposing)
        SettingsNavigationGraph(navController, onComposing)
    }
}