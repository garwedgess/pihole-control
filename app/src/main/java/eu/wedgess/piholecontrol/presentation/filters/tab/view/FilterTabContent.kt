package eu.wedgess.piholecontrol.presentation.filters.tab.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun FilterListContent(
    filtersList: List<FilterRuleInfo>,
    onFilterRuleClick: (FilterRuleInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = rememberLazyListState()
    ) {
        items(filtersList, key = { it.id }) {
            FilterRuleItem(
                modifier = Modifier.animateItem(),
                rule = it,
                onItemClick = { onFilterRuleClick(it) }
            )
        }
    }
}

@ThemePreview
@Composable
private fun FilterListContentPreview() {
    PiHoleControlTheme {
        Surface {
            FilterListContent(
                filtersList = listOf(
                    FilterRuleInfo(
                        id = 0,
                        dateAdded = "14-01-2022",
                        dateModified = "14-01-2023",
                        type = FilterRuleTypeEntity.DENY,
                        domain = "www.google.com",
                        enabled = true,
                        comment = null,
                        groups = emptyList()
                    ),
                    FilterRuleInfo(
                        id = 1,
                        dateAdded = "14-01-2022",
                        dateModified = "14-01-2023",
                        type = FilterRuleTypeEntity.REGEX_DENY,
                        domain = "r[0-9]*----sn-.*.*.*.googlevideo.com",
                        comment = "YouTube Ads",
                        enabled = false,
                        groups = emptyList()
                    )
                ),
                onFilterRuleClick = {}
            )
        }
    }
}
