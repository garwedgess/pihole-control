package eu.wedgess.piholecontrol.presentation.dashboard.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.DoublePressToExitBackHandler
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.dashboard.DashboardContract
import eu.wedgess.piholecontrol.presentation.dashboard.view.DashboardScreen
import eu.wedgess.piholecontrol.presentation.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.dashboardRoot(
    onComposing: (AppBarState) -> Unit,
    showSnackBarText: (UiText) -> Unit
) {
    composable<Screens.Dashboard> {
        val viewModel: DashboardViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()
        val sideEffect = viewModel.sideEffect
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            onComposing(AppBarState.Normal(showNavigateBackIcon = false))
        }
        CollectSideEffect(sideEffect) { effect ->
            if (effect is DashboardContract.Effect.ShowErrorSnackbar) {
                showSnackBarText(
                    UiText.DynamicString(
                        effect.errorMessages.joinToString(separator = "\n") {
                            it.asString(context)
                        }
                    )
                )
            }
        }

        DashboardScreen(uiResult)
        DoublePressToExitBackHandler()
    }
}
