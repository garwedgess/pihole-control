package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.compose.CollectSideEffect
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.LoadingScreen
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.settings.SettingsContract
import eu.wedgess.mihole.ui.settings.view.SettingsScreen
import eu.wedgess.mihole.ui.settings.viewmodel.SettingsViewModel
import eu.wedgess.mihole.utils.UiText

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
                    title = UiText.StringResource(id = R.string.nav_title_settings)
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