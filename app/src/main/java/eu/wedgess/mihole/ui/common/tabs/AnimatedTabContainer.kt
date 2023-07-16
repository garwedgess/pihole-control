package eu.wedgess.mihole.ui.common.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.ui.base.TabItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedTabContainer(
    tabItems: List<TabItem>,
    modifier: Modifier = Modifier,
    onTabIndexChanged: ((index: Int) -> Unit)? = null
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = {
        tabItems.size
    })

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                        onTabIndexChanged?.invoke(index)
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
            tabItems[page].screen()
        }
    }
}