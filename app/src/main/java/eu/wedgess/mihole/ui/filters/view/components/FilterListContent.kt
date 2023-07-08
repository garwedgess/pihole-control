package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.ui.common.search.SearchStatus
import eu.wedgess.mihole.ui.filters.FiltersContract
import java.text.DateFormat

@Composable
fun FilterListContent(
    filtersList: List<PiHoleFilterRules.PiHoleFilterRule>,
    searchState: SearchState<PiHoleFilterRules.PiHoleFilterRule>,
    onEvent: (FiltersContract.Event) -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getDateInstance() }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        when (searchState.searchDisplay) {
            SearchStatus.InitialResults -> {
                items(filtersList, key = { it.id }) { filterRule ->
                    FilterRuleItem(
                        rule = filterRule,
                        dateFormat = dateTimeInstance,
                        onItemClicked = { onEvent(FiltersContract.Event.OnRuleSelected(filterRule)) }
                    )
                }
            }

            SearchStatus.SearchInProgress, SearchStatus.Results -> {
                items(searchState.searchResults, key = { it.id }) { filterRule ->
                    FilterRuleItem(
                        rule = filterRule,
                        dateFormat = dateTimeInstance,
                        onItemClicked = { onEvent(FiltersContract.Event.OnRuleSelected(filterRule)) }
                    )
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