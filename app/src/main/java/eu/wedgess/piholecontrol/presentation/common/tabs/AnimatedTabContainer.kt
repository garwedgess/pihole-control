package eu.wedgess.piholecontrol.presentation.common.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.base.TabItem
import kotlinx.coroutines.launch

@Composable
fun <T : TabItem> AnimatedTabContainer(
    tabItems: List<T>,
    modifier: Modifier = Modifier,
    onTabIndexChanged: ((index: Int) -> Unit)? = null,
    onTabSelected: @Composable (T) -> Unit
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        tabItems.size
    })
    LaunchedEffect(key1 = pagerState.settledPage) {
        onTabIndexChanged?.invoke(pagerState.settledPage)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(tab.title.asString()) },
                    icon = {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title.asString()
                        )
                    }
                )
            }
        }
        HorizontalPager(state = pagerState) { page ->
            onTabSelected(tabItems[page])
        }
    }
}