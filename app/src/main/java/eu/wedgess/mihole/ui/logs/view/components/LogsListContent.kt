package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.PiHoleLog
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
                    LogItem(log)
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
                    LogItem(log)
                }
            }

            SearchStatus.NoResults -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No results found for: ${searchState.query.text}")
                    }
                }
            }
        }

    }
}