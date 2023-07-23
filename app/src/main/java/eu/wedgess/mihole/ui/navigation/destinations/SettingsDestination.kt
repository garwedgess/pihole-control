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
        route = Screens.Settings.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route,
                Screens.Filters.route,
                Screens.Logs.route ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300)
                    )

                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route,
                Screens.Filters.route,
                Screens.Logs.route ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300)
                    )

                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route,
                Screens.Filters.route,
                Screens.Logs.route ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(300)
                    )

                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route,
                Screens.Filters.route,
                Screens.Logs.route ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(300)
                    )

                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            }
        }
    ) {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_settings)
                )
            )
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