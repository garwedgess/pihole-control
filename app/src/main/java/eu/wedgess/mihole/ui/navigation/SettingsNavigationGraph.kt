package eu.wedgess.mihole.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.navigation.destinations.ConnectionsDestination
import eu.wedgess.mihole.ui.navigation.destinations.ModifyConnectionDestination
import eu.wedgess.mihole.ui.navigation.destinations.SettingsDestination

fun NavGraphBuilder.SettingsNavigationGraph(
    navHostController: NavHostController,
    onComposing: (AppBarState) -> Unit,
) {
    navigation(
        route = Graphs.Settings.route,
        startDestination = Screens.Settings.route
    ) {
        SettingsDestination(
            onComposing,
            onNavigateToConnections = { navHostController.navigate(Screens.Connections.route) }
        )
        ConnectionsDestination(onComposing,
            navigateToModifyConnection = {
                it?.run {
                    navHostController.navigate(Screens.ModifyConnection.routeWithArgs(this))
                }
            },
            navigateToCreateConnection = {
                navHostController.navigate(Screens.ModifyConnection.routeWithNoArgs())
            }
        )
        ModifyConnectionDestination(
            onComposing,
            onNavigateBack = { navHostController.navigateUp() }
        )
    }
}