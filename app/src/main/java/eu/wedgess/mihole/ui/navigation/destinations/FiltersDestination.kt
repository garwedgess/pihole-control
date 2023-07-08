package eu.wedgess.mihole.ui.navigation.destinations

import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.FiltersScreen
import eu.wedgess.mihole.ui.filters.view.components.FilterTopBarActions
import eu.wedgess.mihole.ui.filters.view.components.SearchContent
import eu.wedgess.mihole.ui.filters.viewmodel.FiltersViewModel
import eu.wedgess.mihole.ui.navigation.Screens
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
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(300))
                else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(300))
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route ->
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(300))
                else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(300))
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route ->
                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(300))
                else -> slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(300))
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                Screens.Dashboard.route,
                Screens.Statistics.route ->
                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(300))
                else -> slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(300))
            }
        }
    ) {
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(uiState.showSearchView, uiState.allowList, uiState.blockList) {
            onComposing(
                AppBarState(
                    title = "Filters",
                    actions = { FilterTopBarActions(onSearchClicked = { viewModel.onEvent(FiltersContract.Event.OnShowSearchView) }) },
                    showSearchView = uiState.allowList is Resource.Success && uiState.blockList is Resource.Success && uiState.showSearchView,
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
            do {
                viewModel.onEvent(FiltersContract.Event.FetchRulesList)
                delay(10_000)
            } while (true)
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when(effect) {
                    is FiltersContract.Effect.Toast -> Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // https://github1s.com/SmartToolFactory/Jetpack-Compose-Tutorials/blob/HEAD/Tutorial1-1Basics/src/main/java/com/smarttoolfactory/tutorial1_1basics/Search.kt

        FiltersScreen(
            uiState,
            viewModel::onEvent
        )
    }
}