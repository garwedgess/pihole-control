package eu.wedgess.piholecontrol.ui.navigation

import androidx.camera.core.ExperimentalGetImage
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.navigation.destinations.ConnectionsDestination
import eu.wedgess.piholecontrol.ui.navigation.destinations.ModifyConnectionDestination
import eu.wedgess.piholecontrol.ui.navigation.destinations.SettingsDestination

@ExperimentalGetImage
fun NavGraphBuilder.SettingsNavigationGraph(
    navHostController: NavHostController,
    onComposing: (AppBarState) -> Unit,
) {
    navigation(
        route = Graphs.Settings.route,
        startDestination = Screens.Settings.route,
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