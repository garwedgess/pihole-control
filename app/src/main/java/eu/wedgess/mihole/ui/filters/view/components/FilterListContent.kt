package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.ui.filters.FiltersContract
import java.text.DateFormat

@Composable
fun FilterListContent(
    filtersList: List<PiHoleFilterRules.PiHoleFilterRule>,
    onEvent: (FiltersContract.Event) -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getDateInstance() }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        items(filtersList, key = { it.id }) { filterRule ->
            FilterRuleItem(
                rule = filterRule,
                dateFormat = dateTimeInstance,
                onItemClicked = { onEvent(FiltersContract.Event.OnRuleSelected(filterRule)) }
            )
        }
    }
}