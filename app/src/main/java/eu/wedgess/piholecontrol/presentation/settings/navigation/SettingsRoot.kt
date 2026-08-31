package eu.wedgess.piholecontrol.presentation.settings.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.DoublePressToExitBackHandler
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.settings.SettingsContract
import eu.wedgess.piholecontrol.presentation.settings.view.SettingsScreen
import eu.wedgess.piholecontrol.presentation.settings.viewmodel.SettingsViewModel
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.settingsRoot(
    onComposing: (AppBarState) -> Unit,
    onNavigateToConnections: () -> Unit
) {
    composable<Screens.Settings> {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()
        val appBarState = remember {
            AppBarState.Normal(
                title = UiText.StringResource(id = R.string.nav_title_settings),
                showNavigateBackIcon = true,
                displayConnection = false,
                bottomBarVisible = false,
                showSettingsAction = false
            )
        }

        LaunchedEffect(appBarState) {
            onComposing(appBarState)
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
            when (effect) {
                is SettingsContract.Effect.Navigation.Connections -> {
                    onNavigateToConnections()
                }
            }
        }

        uiResult.Compose(
            onLoaded = { SettingsScreen(it, viewModel::onEvent) },
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) }
        )
        DoublePressToExitBackHandler()
    }
}
