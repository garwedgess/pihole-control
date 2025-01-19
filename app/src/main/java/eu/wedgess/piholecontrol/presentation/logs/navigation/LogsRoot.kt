package eu.wedgess.piholecontrol.presentation.logs.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.SearchContent
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.logs.view.LogsScreen
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
        var appbarState by remember { mutableStateOf(AppBarState()) }
        val scaffoldState = rememberBottomSheetScaffoldState()
        val snackbarHostState = remember { SnackbarHostState() }

        uiResult.run {
            if (this is UIResult.Loaded) {
                val state = this.data
                LaunchedEffect(state.showSortingDropdownMenu) {
                    appbarState = appbarState.copy(
                        actions = {
                            LogsTopBarActions(
                                onSearchClick = {
                                    viewModel.onEvent(LogsContract.Event.OnShowSearchView)
                                },
                                onSortClick = {
                                    viewModel.onEvent(LogsContract.Event.OnShowSortingMenu)
                                },
                                onSortItemClick = {
                                    viewModel.onEvent(LogsContract.Event.OnSortTypeSelected(it))
                                },
                                onDismissSort = {
                                    viewModel.onEvent(LogsContract.Event.OnSortingDismissed)
                                },
                                isSortingMenuVisible = state.showSortingDropdownMenu,
                                selectedSorting = state.sorting
                            )
                        }
                    )
                    onComposing(appbarState)
                }
            }
        }

        LaunchedEffect(searchUiState.showSearchView, searchUiState.searchQuery) {
            appbarState = appbarState.copy(
                showSearchView = searchUiState.showSearchView,
                searchContent = {
                    SearchContent(
                        placeHolderText = context.getString(
                            R.string.logs_search_placeholder
                        ),
                        searchQuery = searchUiState.searchQuery,
                        showSearchView = searchUiState.showSearchView,
                        onExpandedChange = {
                            viewModel.onEvent(
                                LogsContract.Event.OnSearchExpandedChanged(it)
                            )
                        },
                        onSearch = {
                            viewModel.onEvent(LogsContract.Event.OnSearchClick)
                        },
                        onQueryChange = {
                            viewModel.onEvent(LogsContract.Event.OnSearchQueryChanged(it))
                        },
                        onClearSearchQuery = {
                            viewModel.onEvent(LogsContract.Event.OnClearSearchQuery(it))
                        }
                    )
                }
            )
            onComposing(appbarState)
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
    }
}

private suspend fun LogsContract.Effect.Snackbar.handleDisplayingSnackbar(
    snackbarHostState: SnackbarHostState,
    context: Context,
    onRetryAddToAllowList: (String) -> Unit,
    onRetryAddToDenyList: (String) -> Unit,
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
