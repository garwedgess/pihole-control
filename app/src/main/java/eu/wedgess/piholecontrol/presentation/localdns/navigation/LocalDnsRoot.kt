package eu.wedgess.piholecontrol.presentation.localdns.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.components.SearchContent
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.localdns.LocalDnsContract
import eu.wedgess.piholecontrol.presentation.localdns.view.LocalDnsScreen
import eu.wedgess.piholecontrol.presentation.localdns.view.components.actions.LocalDnsTopBarActions
import eu.wedgess.piholecontrol.presentation.localdns.viewmodel.LocalDnsViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.localDnsRoot(
    onComposing: (AppBarState) -> Unit
) {
    composable<Screens.LocalDns> {
        val viewModel: LocalDnsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(uiState.showSearchView, uiState.searchQuery) {
            onComposing(
                AppBarState(
                    title = UiText.StringResource(id = R.string.nav_title_local_dns),
                    showNavigateBackIcon = !uiState.showSearchView,
                    displayConnection = false,
                    bottomBarVisible = false,
                    actions = {
                        LocalDnsTopBarActions(
                            onSearchClick = {
                                viewModel.onEvent(LocalDnsContract.Event.OnShowSearchView)
                            }
                        )
                    },
                    showSearchView = uiState.showSearchView,
                    searchContent = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            SearchContent(
                                placeHolderText = context.getString(R.string.local_dns_search_placeholder),
                                searchQuery = uiState.searchQuery,
                                showSearchView = uiState.showSearchView,
                                onExpandedChange = {
                                    viewModel.onEvent(
                                        LocalDnsContract.Event.OnSearchExpandedChanged(it)
                                    )
                                },
                                onQueryChange = {
                                    viewModel.onEvent(
                                        LocalDnsContract.Event.OnSearchQueryChanged(it)
                                    )
                                },
                                onClearSearchQuery = {
                                    viewModel.onEvent(LocalDnsContract.Event.OnClearSearchQuery(it))
                                }
                            )
                        }
                    }
                )
            )
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
            when (effect) {
                is LocalDnsContract.Effect.Toast -> {
                    Toast.makeText(
                        context,
                        effect.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        LocalDnsScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}
