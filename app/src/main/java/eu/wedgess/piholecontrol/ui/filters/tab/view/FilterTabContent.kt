package eu.wedgess.piholecontrol.ui.filters.tab.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRules

@Composable
fun FilterListContent(
    filtersList: List<PiHoleFilterRules.PiHoleFilterRule>,
    onFilterRuleClick: (PiHoleFilterRules.PiHoleFilterRule) -> Unit
) {

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        items(filtersList) {
            FilterRuleItem(
                rule = it,
                onItemClicked = { onFilterRuleClick(it) }
            )
        }
    }
}