package eu.wedgess.mihole.ui.common.tabs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import timber.log.Timber

@Composable
fun AnimatedTabContainer(
    tabItems: List<TabItem>,
    onTabIndexChanged: ((index: Int) -> Unit)? = null
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var previousTabIndex by remember { mutableIntStateOf(0) }
    var isSwipeToTheLeft by remember { mutableStateOf(false) }
    val dragState = rememberDraggableState(onDelta = { delta ->
        isSwipeToTheLeft = delta > 0
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        previousTabIndex = selectedTabIndex
                        selectedTabIndex = index
                        onTabIndexChanged?.invoke(index)
                    },
                    text = { Text(tab.title) },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) }
                )
            }
        }
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = if (selectedTabIndex < previousTabIndex) {
                        AnimatedContentTransitionScope.SlideDirection.End
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Start
                    },
                    initialOffset = { it }
                ).togetherWith(
                    slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = if (selectedTabIndex < previousTabIndex) {
                            AnimatedContentTransitionScope.SlideDirection.Start
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.End
                        },
                        targetOffset = { -it }
                    )
                )
            }
        ) { targetState ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .draggable(
                        state = dragState,
                        orientation = Orientation.Horizontal,
                        onDragStarted = { },
                        onDragStopped = {
                            val newIndex = setSelectedIndexBasedOnSwipe(
                                isSwipeLeft = isSwipeToTheLeft,
                                currentIndex = selectedTabIndex,
                                numOfTabs = tabItems.size.minus(1)
                            )
                            if (newIndex != selectedTabIndex) {
                                previousTabIndex = selectedTabIndex
                                selectedTabIndex = newIndex
                            }
                        })
            ) {
                tabItems[targetState].screen()
            }
        }
    }
}

private fun setSelectedIndexBasedOnSwipe(
    isSwipeLeft: Boolean,
    currentIndex: Int,
    numOfTabs: Int
): Int {
    return if (isSwipeLeft) {
        currentIndex.minus(1).coerceAtLeast(0)
    } else {
        currentIndex.plus(1).coerceAtMost(numOfTabs)
    }
}