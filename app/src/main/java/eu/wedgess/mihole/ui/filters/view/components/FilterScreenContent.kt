package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import eu.wedgess.mihole.ui.common.tabs.FancyIndicatorContainerTabs
import eu.wedgess.mihole.ui.common.tabs.TabItem
import eu.wedgess.mihole.ui.filters.FiltersContract

@Composable
fun FilterScreenContent(
    uiState: FiltersContract.UiState,
    onEvent: (FiltersContract.Event) -> Unit
) {
    val tabItems = remember(uiState.allowList, uiState.blockList) {
        mutableListOf(
            TabItem(
                title = "Allow List",
                icon = Icons.Default.CheckCircleOutline,
                screen = { FilterScreenTabContent(uiState.allowList, onEvent) }
            ),
            TabItem(
                title = "Block List",
                icon = Icons.Default.Block,
                screen = { FilterScreenTabContent(uiState.blockList, onEvent) }
            )
        )
    }
    FancyIndicatorContainerTabs(
        tabItems = tabItems,
        onTabIndexChanged = { onEvent(FiltersContract.Event.OnTabIndexChanged(it)) }
    )
    FilterDialogs(
        selectedRule = uiState.selectedRule,
        displayAddRuleDialog = uiState.displayAddRuleDialog,
        onEvent = onEvent
    )
}