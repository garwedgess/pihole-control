package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.search.SearchContent
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.logs.view.LogsScreen
import eu.wedgess.mihole.ui.logs.view.components.actions.LogsTopBarActions
import eu.wedgess.mihole.ui.logs.viewmodel.LogsViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.LogsDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Logs.route,
    ) {
        val viewModel: LogsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(uiState.showSearchView, uiState.logs) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_logs),
                    actions = {
                        LogsTopBarActions(
                            onSearchClicked = { viewModel.onEvent(LogsContract.Event.OnShowSearchView) },
                            onSortClicked = { viewModel.onEvent(LogsContract.Event.OnShowSortingBottomSheet) },
                            onSortItemSelected = {
                                viewModel.onEvent(
                                    LogsContract.Event.OnSortTypeSelected(
                                        it
                                    )
                                )
                            },
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
            viewModel.onEvent(LogsContract.Event.FetchLogs)
            viewModel.onEvent(LogsContract.Event.ListenForConnectionChanges)
        }

//        DisposableEffect(Unit) {
//            val refreshJob = viewModel.autoRefreshData()
//
//            onDispose {
//                refreshJob.cancel()
//            }
//        }

        val scaffoldState = rememberBottomSheetScaffoldState()
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is LogsContract.Effect.ShowBottomSheet -> scaffoldState.bottomSheetState.expand()
                    is LogsContract.Effect.Snackbar -> {
                        snackbarHostState.showSnackbar(
                            message = effect.message.asString(context),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }

        LogsScreen(
            uiState = uiState,
            scaffoldState = scaffoldState,
            snackbarHostState = snackbarHostState,
            onEvent = viewModel::onEvent
        )
    }
}