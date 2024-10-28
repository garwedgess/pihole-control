package eu.wedgess.mihole.ui.navigation.destinations

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.FiltersScreen
import eu.wedgess.mihole.ui.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.mihole.ui.filters.viewmodel.FiltersViewModel
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.FiltersDestination(
    onComposing: (AppBarState) -> Unit
) {
    composable(
        route = Screens.Filters.route
    ) {
        val viewModel: FiltersViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

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
                        androidx.compose.material3.SearchBar(
                            inputField = {
                                SearchBarDefaults.InputField(
                                    onSearch = {
                                        viewModel.onEvent(FiltersContract.Event.OnSearchClick)
                                    },
                                    query = uiState.searchQuery,
                                    onQueryChange = {
                                        viewModel.onEvent(
                                            FiltersContract.Event.OnSearchQueryChanged(
                                                it
                                            )
                                        )
                                    },
                                    expanded = uiState.showSearchView,
                                    onExpandedChange = {
                                        viewModel.onEvent(
                                            FiltersContract.Event.OnSearchExpandedChanged(
                                                it
                                            )
                                        )
                                    },
                                    placeholder = { Text("Search for filter...") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            viewModel.onEvent(
                                                FiltersContract.Event.OnClearSearchQuery(
                                                    uiState.searchQuery
                                                )
                                            )
                                        }) {
                                            Icon(Icons.Default.Close, contentDescription = null)
                                        }
                                    },
                                )
                            },
                            expanded = uiState.showSearchView,
                            onExpandedChange = {
                                viewModel.onEvent(FiltersContract.Event.OnSearchExpandedChanged(it))
                            }
                        ) { }
                    }
                )
            )
        }

        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is FiltersContract.Effect.Toast -> Toast.makeText(
                        context,
                        effect.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        FiltersScreen(uiState, viewModel::onEvent)
    }
}