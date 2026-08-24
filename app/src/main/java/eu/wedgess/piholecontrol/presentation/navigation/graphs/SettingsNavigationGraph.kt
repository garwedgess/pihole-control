package eu.wedgess.piholecontrol.presentation.navigation.graphs

import androidx.camera.core.ExperimentalGetImage
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.connections.list.navigation.connectionsRoot
import eu.wedgess.piholecontrol.presentation.connections.modify.navigation.modifyConnectionRoot
import eu.wedgess.piholecontrol.presentation.localdns.navigation.localDnsRoot
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.settings.navigation.settingsRoot

@ExperimentalGetImage
fun NavGraphBuilder.settingsNavigationGraph(
    navHostController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    navigation<Graphs.Settings>(
        startDestination = Screens.Settings
    ) {
        settingsRoot(
            onComposing = onComposing,
            onNavigateToConnections = { navHostController.navigate(Screens.Connections) },
            onNavigateToLocalDns = { navHostController.navigate(Screens.LocalDns) }
        )
        connectionsRoot(
            onComposing = onComposing,
            navigateToModifyConnection = {
                it?.run {
                    navHostController.navigate(Screens.ModifyConnection(this.toString()))
                }
            },
            navigateToCreateConnection = {
                navHostController.navigate(Screens.ModifyConnection())
            }
        )
        modifyConnectionRoot(
            onComposing = onComposing,
            onNavigateBack = { navHostController.navigateUp() }
        )
        localDnsRoot(
            onComposing = onComposing
        )
    }
}
