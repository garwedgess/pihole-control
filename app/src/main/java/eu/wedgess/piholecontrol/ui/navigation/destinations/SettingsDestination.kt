package eu.wedgess.piholecontrol.ui.navigation.destinations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.compose.CollectSideEffect
import eu.wedgess.piholecontrol.ui.compose.Compose
import eu.wedgess.piholecontrol.ui.compose.LoadingScreen
import eu.wedgess.piholecontrol.ui.navigation.Screens
import eu.wedgess.piholecontrol.ui.settings.SettingsContract
import eu.wedgess.piholecontrol.ui.settings.view.SettingsScreen
import eu.wedgess.piholecontrol.ui.settings.viewmodel.SettingsViewModel
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.SettingsDestination(
    onComposing: (AppBarState) -> Unit,
    onNavigateToConnections: () -> Unit
) {
    composable(
        route = Screens.Settings.route
    ) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_settings),
                    bottomBarVisible = true
                )
            )
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
    }
}