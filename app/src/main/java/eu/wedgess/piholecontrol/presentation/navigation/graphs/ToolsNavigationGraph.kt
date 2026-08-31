package eu.wedgess.piholecontrol.presentation.navigation.graphs

import androidx.camera.core.ExperimentalGetImage
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.connections.list.navigation.connectionsRoot
import eu.wedgess.piholecontrol.presentation.connections.modify.navigation.modifyConnectionRoot
import eu.wedgess.piholecontrol.presentation.diagnosis.navigation.diagnosisRoot
import eu.wedgess.piholecontrol.presentation.localdns.navigation.localDnsRoot
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.settings.navigation.settingsRoot
import eu.wedgess.piholecontrol.presentation.tools.navigation.toolsRoot

@ExperimentalGetImage
fun NavGraphBuilder.toolsNavigationGraph(
    navHostController: NavHostController,
    onComposing: (AppBarState) -> Unit
) {
    navigation<Graphs.Tools>(
        startDestination = Screens.Tools
    ) {
        toolsRoot(
            onComposing = onComposing,
            onNavigateToLocalDns = { navHostController.navigate(Screens.LocalDns) },
            onNavigateToDiagnosis = { navHostController.navigate(Screens.Diagnosis) }
        )
        settingsRoot(
            onComposing = onComposing,
            onNavigateToConnections = { navHostController.navigate(Screens.Connections) }
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
        diagnosisRoot(
            onComposing = onComposing
        )
    }
}
