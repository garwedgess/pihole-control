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
fun NavigationGraph(
    navController: NavHostController,
    onComposing: (AppBarState) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard.route,
        modifier = modifier
    ) {
        DashboardDestination(onComposing)
        StatisticsDestination(onComposing)
        FiltersDestination(onComposing)
        LogsDestination(onComposing)
    }
}