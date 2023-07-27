package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.dashboard.DashboardContract
import eu.wedgess.mihole.ui.dashboard.view.DashboardScreen
import eu.wedgess.mihole.ui.dashboard.viewmodel.DashboardViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.DashboardDestination() {
    composable(
        route = Screens.Dashboard.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        }
    ) {
        val viewModel: DashboardViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.onEvent(DashboardContract.Event.ListenForConnectionChanges)
            viewModel.onEvent(DashboardContract.Event.FetchSummary)
            viewModel.onEvent(DashboardContract.Event.FetchQueriesOvertime)
            viewModel.onEvent(DashboardContract.Event.FetchClientQueriesOvertime)
        }

        DisposableEffect(Unit) {
            val refreshJob = viewModel.autoRefreshData()

            onDispose {
                refreshJob.cancel()
            }
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->

            }
        }

        DashboardScreen(
            uiState,
            viewModel::onEvent
        )
    }
}