package eu.wedgess.mihole.ui.navigation.destinations

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.search.SearchContent
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.FiltersScreen
import eu.wedgess.mihole.ui.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.mihole.ui.filters.viewmodel.FiltersViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

fun NavGraphBuilder.FiltersDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Filters.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route ->
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
                Screens.Statistics.route ->
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
                Screens.Statistics.route ->
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
                Screens.Statistics.route ->
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
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(uiState.showSearchView, uiState.allowList, uiState.blockList) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_filters),
                    actions = { FilterTopBarActions(onSearchClicked = { viewModel.onEvent(FiltersContract.Event.OnShowSearchView) }) },
                    showSearchView = uiState.allowList is UiResult.Success && uiState.blockList is UiResult.Success && uiState.showSearchView,
                    searchContent = {
                        SearchContent(
                            state = uiState.searchState,
                            onQueryChanged = { viewModel.onEvent(FiltersContract.Event.OnSearchQueryChanged(it)) },
                            onClosed = { viewModel.onEvent(FiltersContract.Event.OnHideShowSearchView) }
                        )
                    }
                )
            )
        }

        LaunchedEffect(Unit) {
            viewModel.onEvent(FiltersContract.Event.FetchRulesList)
            viewModel.onEvent(FiltersContract.Event.ListenForConnectionChanges)
        }

        DisposableEffect(Unit) {
            val refreshJob = viewModel.autoRefreshData()

            onDispose {
                refreshJob.cancel()
            }
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is FiltersContract.Effect.Toast -> Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        FiltersScreen(
            uiState,
            viewModel::onEvent
        )
    }
}