package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.responses.PiHoleLog
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.ui.common.search.SearchStatus
import eu.wedgess.mihole.ui.logs.LogsContract

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogsListContent(
    logsList: List<PiHoleLog>,
    searchState: SearchState<PiHoleLog>,
    onEvent: (LogsContract.Event) -> Unit
) {

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        when (searchState.searchDisplay) {
            SearchStatus.InitialResults -> {
                stickyHeader {
                    LogsListStickyHeader(
                        listSize = logsList.size,
                        onFilterButtonClicked = { onEvent(LogsContract.Event.OnShowFiltersBottomSheet) }
                    )
                }
                items(logsList) { log ->
                    LogListItem(log, onItemClicked = { onEvent(LogsContract.Event.OnLogSelected(log)) })
                }
            }

            SearchStatus.SearchInProgress, SearchStatus.Results -> {
                stickyHeader {
                    LogsListStickyHeader(
                        listSize = searchState.searchResults.size,
                        onFilterButtonClicked = { onEvent(LogsContract.Event.OnShowFiltersBottomSheet) }
                    )

                }
                items(searchState.searchResults) { log ->
                    LogListItem(log, onItemClicked = { onEvent(LogsContract.Event.OnLogSelected(log)) })
                }
            }

            SearchStatus.NoResults -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.all_msg_no_results_found_for,
                                searchState.query.text
                            )
                        )
                    }
                }
            }
        }

    }
}