package eu.wedgess.mihole.ui.navigation.destinations

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.common.SearchContent
import eu.wedgess.mihole.ui.compose.CollectSideEffect
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.FiltersScreen
import eu.wedgess.mihole.ui.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.mihole.ui.filters.viewmodel.FiltersViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText

fun NavGraphBuilder.FiltersDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Filters.route
    ) {
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        var refreshTrigger: () -> Unit = {}

        LaunchedEffect(uiState.showSearchView) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_filters),
                    actions = {
                        FilterTopBarActions(onSearchClicked = {
                            viewModel.onEvent(
                                FiltersContract.Event.OnShowSearchView
                            )
                        })
                    },
                    showSearchView = uiState.showSearchView,
                    searchContent = {
                        SearchContent(
                            placeHolderText = "Search for filter...",
                            searchQuery = uiState.searchQuery,
                            showSearchView = uiState.showSearchView,
                            onExpandedChange = {
                                viewModel.onEvent(
                                    FiltersContract.Event.OnSearchExpandedChanged(
                                        it
                                    )
                                )
                            },
                            onSearch = { viewModel.onEvent(FiltersContract.Event.OnSearchClick) },
                            onQueryChange = {
                                viewModel.onEvent(
                                    FiltersContract.Event.OnSearchQueryChanged(
                                        it
                                    )
                                )
                            },
                            onClearSearchQuery = {
                                viewModel.onEvent(FiltersContract.Event.OnClearSearchQuery(it))
                            }
                        )
                    }
                )
            )
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
            when (effect) {
                is FiltersContract.Effect.Toast -> {
                    refreshTrigger.invoke()
                    Toast.makeText(
                        context,
                        effect.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        FiltersScreen(uiState, viewModel::onEvent, triggerRefreshEvent = {
            refreshTrigger = it
        })
    }
}