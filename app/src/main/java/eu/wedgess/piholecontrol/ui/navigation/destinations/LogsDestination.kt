package eu.wedgess.piholecontrol.ui.navigation.destinations

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.common.SearchContent
import eu.wedgess.piholecontrol.ui.compose.CollectSideEffect
import eu.wedgess.piholecontrol.ui.compose.UIResult
import eu.wedgess.piholecontrol.ui.logs.LogsContract
import eu.wedgess.piholecontrol.ui.logs.view.LogsScreen
import eu.wedgess.piholecontrol.ui.logs.view.components.actions.LogsTopBarActions
import eu.wedgess.piholecontrol.ui.logs.viewmodel.LogsViewModel
import eu.wedgess.piholecontrol.ui.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.LogsDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Logs.route,
    ) {
        val viewModel: LogsViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()
        val sideEffect = viewModel.sideEffect
        val context = LocalContext.current

        LaunchedEffect(
            key1 = (uiResult as? UIResult.Loaded)?.data?.showSearchView,
            key2 = (uiResult as? UIResult.Loaded)?.data?.logs
        ) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_logs),
                    actions = {
                        (uiResult as? UIResult.Loaded)?.data?.run {
                            LogsTopBarActions(
                                onSearchClicked = { viewModel.onEvent(LogsContract.Event.OnShowSearchView) },
                                onSortClicked = { viewModel.onEvent(LogsContract.Event.OnShowSortingMenu) },
                                onSortItemSelected = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnSortTypeSelected(
                                            it
                                        )
                                    )
                                },
                                onSortDismissed = { viewModel.onEvent(LogsContract.Event.OnSortingDismissed) },
                                isSortingMenuVisible = this.showSortingDropdownMenu,
                                selectedSorting = this.sorting
                            )
                        }
                    },
                    showSearchView = (uiResult as? UIResult.Loaded)?.data?.showSearchView == true,
                    searchContent = {
                        (uiResult as? UIResult.Loaded)?.data?.run {
                            SearchContent(
                                placeHolderText = "Search for logs...",
                                searchQuery = this.searchQuery,
                                showSearchView = this.showSearchView,
                                onExpandedChange = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnSearchExpandedChanged(
                                            it
                                        )
                                    )
                                },
                                onSearch = { viewModel.onEvent(LogsContract.Event.OnSearchClick) },
                                onQueryChange = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnSearchQueryChanged(
                                            it
                                        )
                                    )
                                },
                                onClearSearchQuery = {
                                    viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(it))
                                }
                            )
                        }
                    }
                )
            )
        }

        val scaffoldState = rememberBottomSheetScaffoldState()
        val snackbarHostState = remember { SnackbarHostState() }

        CollectSideEffect(sideEffect = sideEffect) {
            when (it) {
                is LogsContract.Effect.Snackbar -> {
                    snackbarHostState.showSnackbar(
                        message = it.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

//        LaunchedEffect(Unit) {
//            viewModel.effect.collectLatest { effect ->
//                when (effect) {
//                    is LogsContract.Effect.ShowBottomSheet -> scaffoldState.bottomSheetState.expand()
//                    is LogsContract.Effect.Snackbar -> {
//                        snackbarHostState.showSnackbar(
//                            message = effect.message.asString(context),
//                            duration = SnackbarDuration.Short
//                        )
//                    }
//                }
//            }
//        }

        LogsScreen(
            uiResult = uiResult,
            scaffoldState = scaffoldState,
            snackbarHostState = snackbarHostState,
            onEvent = viewModel::onEvent
        )
    }
}