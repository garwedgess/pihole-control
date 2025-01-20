package eu.wedgess.piholecontrol.presentation.filters.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.SearchContent
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.view.FiltersScreen
import eu.wedgess.piholecontrol.presentation.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.piholecontrol.presentation.filters.viewmodel.FiltersViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.filtersRoot(
    onComposing: (AppBarState) -> Unit
) {
    composable<Screens.Filters> {
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        var refreshTrigger by remember {
            mutableStateOf({})
        }

        LaunchedEffect(uiState.showSearchView) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_filters),
                    actions = {
                        FilterTopBarActions(
                            onSearchClick = {
                                viewModel.onEvent(FiltersContract.Event.OnShowSearchView)
                            },
                            onFilterByClick = {
                                viewModel.onEvent((FiltersContract.Event.OnShowFilterByMenu))
                            },
                            onDismissFiltering = {
                                viewModel.onEvent((FiltersContract.Event.OnDismissFilterBy))
                            },
                            onFilterByOptionSelected = {
                                viewModel.onEvent(FiltersContract.Event.OnFilterByOptionClick(it))
                            },
                            availableFilterByOptions = uiState.filterByOptions,
                            isFilterByMenuVisible = uiState.showFilterByMenu,
                            selectedFilterByOptions = uiState.selectedFilterBy
                        )
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
            triggerRefreshEvent = {
                refreshTrigger = it
            }
        )
    }
}
