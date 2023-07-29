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
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.statistics.StatisticsContract
import eu.wedgess.mihole.ui.statistics.view.StatisticsScreen
import eu.wedgess.mihole.ui.statistics.viewmodel.StatisticsViewModel
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.StatisticsDestination() {
    composable(
        route = Screens.Statistics.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route ->
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
                Screens.Dashboard.route ->
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
                Screens.Dashboard.route ->
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
                Screens.Dashboard.route ->
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
        val viewModel: StatisticsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.onEvent(StatisticsContract.Event.ListenForConnectionChanges)
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

        StatisticsScreen(
            uiState,
            viewModel::onEvent
        )
    }
}