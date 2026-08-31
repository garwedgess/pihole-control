package eu.wedgess.piholecontrol.presentation.tools.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.DoublePressToExitBackHandler
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.tools.view.ToolsScreen
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.toolsRoot(
    onComposing: (AppBarState) -> Unit,
    onNavigateToLocalDns: () -> Unit,
    onNavigateToDiagnosis: () -> Unit
) {
    composable<Screens.Tools> {
        val appBarState = remember {
            AppBarState.Normal(
                title = UiText.StringResource(id = R.string.nav_title_tools)
            )
        }

        LaunchedEffect(appBarState) {
            onComposing(appBarState)
        }

        ToolsScreen(
            onLocalDnsClick = onNavigateToLocalDns,
            onDiagnosisClick = onNavigateToDiagnosis
        )
        DoublePressToExitBackHandler()
    }
}
