package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity

@Composable
fun FilterListContent(
    filtersList: List<FilterRuleEntity>,
    onFilterRuleClick: (FilterRuleEntity) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        items(filtersList) {
            FilterRuleItem(
                rule = it,
                onItemClick = { onFilterRuleClick(it) }
            )
        }
    }
}
