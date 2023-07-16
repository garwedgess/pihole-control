package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.TabItem
import eu.wedgess.mihole.ui.common.tabs.AnimatedTabContainer
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.utils.UiText

@Composable
fun FilterScreenContent(
    uiState: FiltersContract.UiState,
    onEvent: (FiltersContract.Event) -> Unit
) {
    val tabItems = remember(uiState.searchState, uiState.allowList, uiState.blockList) {
        mutableListOf(
            TabItem(
                title = UiText.StringResource(R.string.filters_tab_title_allow_list),
                icon = Icons.Default.CheckCircleOutline,
                screen = { FilterScreenTabContent(uiState.allowList, uiState.searchState, onEvent) }
            ),
            TabItem(
                title = UiText.StringResource(R.string.filters_tab_title_block_list),
                icon = Icons.Default.Block,
                screen = { FilterScreenTabContent(uiState.blockList, uiState.searchState, onEvent) }
            )
        )
    }
    AnimatedTabContainer(
        tabItems = tabItems,
        onTabIndexChanged = { onEvent(FiltersContract.Event.OnTabIndexChanged(it)) }
    )
    FilterDialogs(
        selectedRule = uiState.selectedRule,
        displayAddRuleDialog = uiState.displayAddRuleDialog,
        onEvent = onEvent
    )
}