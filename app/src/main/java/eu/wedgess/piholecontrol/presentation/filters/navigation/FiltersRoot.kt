package eu.wedgess.piholecontrol.presentation.filters.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.SearchContent
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.view.FiltersScreen
import eu.wedgess.piholecontrol.presentation.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.piholecontrol.presentation.filters.viewmodel.FiltersViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.filtersRoot(
    onComposing: (AppBarState) -> Unit,
    showSnackBarText: (UiText) -> Unit
) {
    composable<Screens.Filters> {
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        var refreshTrigger: () -> Unit = {}

        LaunchedEffect(uiState.showSearchView) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_filters),
                    actions = {
                        FilterTopBarActions(onSearchClick = {
                            viewModel.onEvent(
                                FiltersContract.Event.OnShowSearchView
                            )
                        })
                    },
                    showSearchView = uiState.showSearchView,
                    searchContent = {
                        SearchContent(
                            placeHolderText = context.getString(R.string.filter_search_placeholder),
                            searchQuery = uiState.searchQuery,
                            showSearchView = uiState.showSearchView,
                            onExpandedChange = {
                                viewModel.onEvent(
                                    FiltersContract.Event.OnSearchExpandedChanged(it)
                                )
                            },
                            onSearch = { viewModel.onEvent(FiltersContract.Event.OnSearchClick) },
                            onQueryChange = {
                                viewModel.onEvent(
                                    FiltersContract.Event.OnSearchQueryChanged(it)
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

        FiltersScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            triggerRefreshEvent = { refreshTrigger = it },
            showSnackBarText = showSnackBarText
        )
    }
}
