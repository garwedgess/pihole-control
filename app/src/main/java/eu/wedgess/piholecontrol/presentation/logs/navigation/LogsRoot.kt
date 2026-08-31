package eu.wedgess.piholecontrol.presentation.logs.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.DoublePressToExitBackHandler
import eu.wedgess.piholecontrol.presentation.common.components.SearchContent
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.view.LogsScreen
import eu.wedgess.piholecontrol.presentation.logs.view.components.actions.LogsOverflowMenuItems
import eu.wedgess.piholecontrol.presentation.logs.view.components.actions.LogsTopBarActions
import eu.wedgess.piholecontrol.presentation.logs.viewmodel.LogsViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.logsRoot(
    onComposing: (AppBarState) -> Unit
) {
    composable<Screens.Logs> {
        val viewModel: LogsViewModel = hiltViewModel()
        val uiResult by viewModel.uiResult.collectAsStateWithLifecycle()
        val bottomSheetUiState by viewModel.bottomSheetUiState.collectAsStateWithLifecycle()
        val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()
        val sideEffect = viewModel.sideEffect
        val context = LocalContext.current
        var normalAppBarState by remember { mutableStateOf(AppBarState.Normal()) }
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                skipHiddenState = false,
                confirmValueChange = {
                    if (it == SheetValue.Hidden) {
                        viewModel.onEvent(LogsContract.Event.OnToggleFiltersBottomSheet(false))
                    }
                    true
                }
            )
        )
        val snackbarHostState = remember { SnackbarHostState() }

        uiResult.run {
            if (this is UIResult.Loaded) {
                val state = this.data
                LaunchedEffect(
                    state.sorting,
                    bottomSheetUiState.showFilterBottomSheet
                ) {
                    normalAppBarState = normalAppBarState.copy(
                        actions = {
                            LogsTopBarActions(
                                onSearchClick = {
                                    viewModel.onEvent(LogsContract.Event.OnShowSearchView)
                                }
                            )
                        },
                        overflowActions = { dismiss ->
                            LogsOverflowMenuItems(
                                selectedSorting = state.sorting,
                                onFilterClick = {
                                    dismiss()
                                    viewModel.onEvent(
                                        LogsContract.Event.OnToggleFiltersBottomSheet(true)
                                    )
                                },
                                onSortItemClick = {
                                    dismiss()
                                    viewModel.onEvent(LogsContract.Event.OnSortTypeSelected(it))
                                }
                            )
                        }
                    )
                    if (!searchUiState.showSearchView) {
                        onComposing(normalAppBarState)
                    }
                }
            }
        }

        LaunchedEffect(searchUiState.showSearchView, searchUiState.searchQuery) {
            onComposing(
                if (searchUiState.showSearchView) {
                    AppBarState.Search(
                        title = normalAppBarState.title,
                        searchContent = {
                            SearchContent(
                                placeHolderText = stringResource(
                                    R.string.logs_search_placeholder
                                ),
                                searchQuery = searchUiState.searchQuery,
                                showSearchView = true,
                                onExpandedChange = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnSearchExpandedChanged(it)
                                    )
                                },
                                onQueryChange = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnSearchQueryChanged(it)
                                    )
                                },
                                onClearSearchQuery = {
                                    viewModel.onEvent(
                                        LogsContract.Event.OnClearSearchQuery(it)
                                    )
                                }
                            )
                        }
                    )
                } else {
                    normalAppBarState
                }
            )
        }
        LaunchedEffect(bottomSheetUiState.showFilterBottomSheet) {
            if (bottomSheetUiState.showFilterBottomSheet) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.hide()
            }
        }

        CollectSideEffect(sideEffect = sideEffect) { effect ->
            when (effect) {
                is LogsContract.Effect.Snackbar -> {
                    effect.handleDisplayingSnackbar(
                        snackbarHostState = snackbarHostState,
                        context = context,
                        onRetryAddToAllowList = { domain ->
                            viewModel.onEvent(LogsContract.Event.AddToAllowList(domain))
                        },
                        onRetryAddToDenyList = { domain ->
                            viewModel.onEvent(LogsContract.Event.AddToBlockList(domain))
                        }
                    )
                }
            }
        }

        LogsScreen(
            uiResult = uiResult,
            bottomSheetUiState = bottomSheetUiState,
            scaffoldState = scaffoldState,
            snackbarHostState = snackbarHostState,
            onEvent = viewModel::onEvent
        )
        DoublePressToExitBackHandler()
    }
}

private suspend fun LogsContract.Effect.Snackbar.handleDisplayingSnackbar(
    snackbarHostState: SnackbarHostState,
    context: Context,
    onRetryAddToAllowList: (String) -> Unit,
    onRetryAddToDenyList: (String) -> Unit
) {
    val actionLabel = if (
        this is LogsContract.Effect.Snackbar.AddDomainToAllowListFailed ||
        this is LogsContract.Effect.Snackbar.AddDomainToDenyListFailed
    ) {
        context.getString(R.string.all_btn_retry)
    } else {
        null
    }
    val result = snackbarHostState.showSnackbar(
        message = this.message.asString(context),
        duration = SnackbarDuration.Short,
        actionLabel = actionLabel
    )

    if (actionLabel != null) {
        if (result == SnackbarResult.ActionPerformed) {
            if (this is LogsContract.Effect.Snackbar.AddDomainToAllowListFailed) {
                onRetryAddToAllowList(this.domain)
            } else if (this is LogsContract.Effect.Snackbar.AddDomainToDenyListFailed) {
                onRetryAddToDenyList(this.domain)
            }
        }
    }
}
