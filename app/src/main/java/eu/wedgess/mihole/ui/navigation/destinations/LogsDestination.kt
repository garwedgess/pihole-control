package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.filters.view.components.SearchContent
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.view.LogsScreen
import eu.wedgess.mihole.ui.logs.view.components.LogsTopBarActions
import eu.wedgess.mihole.ui.logs.viewmodel.LogsViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.LogsDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Logs.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route,
                Screens.Filters.route ->
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
                Screens.Filters.route ->
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
                Screens.Filters.route ->
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
                Screens.Filters.route ->
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
        val viewModel: LogsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.showSearchView, uiState.logs) {
            onComposing(
                AppBarState(
                    title = "Logs",
                    actions = {
                        LogsTopBarActions(
                            onSearchClicked = { viewModel.onEvent(LogsContract.Event.OnShowSearchView) },
                            onSortClicked = { viewModel.onEvent(LogsContract.Event.OnShowSortingBottomSheet) },
                            onSortItemSelected = { viewModel.onEvent(LogsContract.Event.OnSortTypeSelected(it)) },
                            onSortDismissed = { viewModel.onEvent(LogsContract.Event.OnSortingDismissed) },
                            isSortingMenuVisible = uiState.showSortingDropdownMenu,
                            selectedSorting = uiState.sorting
                        )
                    },
                    showSearchView = uiState.logs is UiResult.Success && uiState.showSearchView,
                    searchContent = {
                        SearchContent(
                            state = uiState.searchState,
                            onQueryChanged = {
                                viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(it))
                            },
                            onClosed = { viewModel.onEvent(LogsContract.Event.OnHideShowSearchView) }
                        )
                    }
                )
            )
        }

        LaunchedEffect(uiState.showSearchView) {
            do {
                viewModel.onEvent(LogsContract.Event.FetchLogs)
                delay(10_000)
            } while (!uiState.showSearchView)
        }

        val scaffoldState = rememberBottomSheetScaffoldState()

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is LogsContract.Effect.ShowBottomSheet -> scaffoldState.bottomSheetState.expand()
                }
            }
        }

        LogsScreen(
            uiState,
            scaffoldState,
            viewModel::onEvent
        )
    }
}