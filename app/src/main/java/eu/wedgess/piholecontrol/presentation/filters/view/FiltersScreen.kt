package eu.wedgess.piholecontrol.presentation.filters.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
import eu.wedgess.piholecontrol.presentation.common.tabs.AnimatedTabContainer
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.tab.view.FilterTabScreenRoot
import eu.wedgess.piholecontrol.presentation.filters.view.components.FilterDialogs
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FiltersScreen(
    uiState: FiltersContract.UiState,
    onEvent: (FiltersContract.Event) -> Unit,
    triggerRefreshEvent: (() -> Unit) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            if (uiState.selectionMode !is SelectionMode.Active) {
                FloatingActionButton(
                    onClick = { onEvent(FiltersContract.Event.AddFilterRuleClick) }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                AnimatedTabContainer(
                    tabItems = uiState.tabOptionItems,
                    onTabIndexChange = {
                        onEvent(
                            FiltersContract.Event.OnFilterTabChanged(
                                uiState.tabOptionItems[it]
                            )
                        )
                    }
                ) { tabType ->
                    when (tabType) {
                        FilterTab.AllowList -> FilterTabScreenRoot(
                            filterScreenTabType = tabType,
                            filterByOptions = uiState.allowSelectedFilterBy,
                            selectionMode = uiState.selectionMode,
                            searchQuery = uiState.searchQuery,
                            onFilterRuleClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemClick(it))
                            },
                            onFilterRuleLongClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemLongClick(it))
                            },
                            onRefreshFilters = triggerRefreshEvent
                        )

                        FilterTab.DenyList -> FilterTabScreenRoot(
                            filterScreenTabType = tabType,
                            filterByOptions = uiState.denySelectedFilterBy,
                            selectionMode = uiState.selectionMode,
                            searchQuery = uiState.searchQuery,
                            onFilterRuleClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemClick(it))
                            },
                            onFilterRuleLongClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemLongClick(it))
                            },
                            onRefreshFilters = triggerRefreshEvent
                        )
                    }
                }
            }
        }
    )
    FilterDialogs(
        dialogType = uiState.dialogType,
        onAddRuleClick = { onEvent(FiltersContract.Event.OnAddFilterRuleConfirmed) },
        onUpdateRuleClick = { onEvent(FiltersContract.Event.OnUpdateFilterRuleConfirmed) },
        onEditRuleClick = { onEvent(FiltersContract.Event.OnEditFilterRuleClick(it)) },
        onDomainChange = { onEvent(FiltersContract.Event.OnFilterRuleDomainChanged(it)) },
        onGroupsChange = { onEvent(FiltersContract.Event.OnFilterRuleGroupsChanged(it)) },
        onCommentChange = { onEvent(FiltersContract.Event.OnFilterRuleCommentChanged(it)) },
        onEnabledChange = { onEvent(FiltersContract.Event.OnFilterRuleEnabledChanged(it)) },
        onRegexChange = { onEvent(FiltersContract.Event.OnFilterRuleRegexChanged(it)) },
        onDeleteRuleClick = { onEvent(FiltersContract.Event.OnDeleteFilterRuleConfirmed(it)) },
        onDeleteSelectedRulesClick = {
            onEvent(FiltersContract.Event.OnDeleteSelectedFilterRulesConfirmed)
        },
        onDismissDialogClick = { onEvent(FiltersContract.Event.OnDismissDialog) }
    )
}
