package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.settings.SettingsContract
import eu.wedgess.mihole.ui.settings.view.SettingsScreen
import eu.wedgess.mihole.ui.settings.viewmodel.SettingsViewModel
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.SettingsDestination(
    onComposing: (AppBarState) -> Unit,
    onNavigateToConnections: () -> Unit
) {
    composable(
        route = Screens.Settings.route
    ) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_settings)
                )
            )
            viewModel.onEvent(SettingsContract.Event.FetchSettings)
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is SettingsContract.Effect.Navigation.Connections -> {
                        onNavigateToConnections()
                    }
                }
            }
        }

        SettingsScreen(uiState, viewModel::onEvent)
    }
}