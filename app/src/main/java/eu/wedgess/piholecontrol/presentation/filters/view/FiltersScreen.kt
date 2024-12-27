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
import eu.wedgess.piholecontrol.presentation.common.tabs.AnimatedTabContainer
import eu.wedgess.piholecontrol.presentation.filters.FiltersContract
import eu.wedgess.piholecontrol.presentation.filters.model.FilterScreenTabType
import eu.wedgess.piholecontrol.presentation.filters.tab.view.FilterTabScreenRoot
import eu.wedgess.piholecontrol.presentation.filters.view.components.FilterDialogs
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import eu.wedgess.piholecontrol.utils.UiText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FiltersScreen(
    uiState: FiltersContract.UiState,
    onEvent: (FiltersContract.Event) -> Unit,
    triggerRefreshEvent: (() -> Unit) -> Unit,
    showSnackBarText: (UiText) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(FiltersContract.Event.AddFilterRuleClick) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                AnimatedTabContainer(tabItems = FilterTab.all()) { item ->
                    onEvent(FiltersContract.Event.OnFilterTabChanged(item.toFilterRuleType()))
                    when (item) {
                        FilterTab.AllowList -> FilterTabScreenRoot(
                            filterScreenTabType = FilterScreenTabType.ALLOW,
                            searchQuery = uiState.searchQuery,
                            onFilterRuleClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemClick(it))
                            },
                            onRefreshFilters = triggerRefreshEvent,
                            showSnackBarText = showSnackBarText
                        )

                        FilterTab.BlockList -> FilterTabScreenRoot(
                            filterScreenTabType = FilterScreenTabType.BLOCK,
                            searchQuery = uiState.searchQuery,
                            onFilterRuleClick = {
                                onEvent(FiltersContract.Event.OnFilterRuleItemClick(it))
                            },
                            onRefreshFilters = triggerRefreshEvent,
                            showSnackBarText = showSnackBarText
                        )
                    }
                }
            }
        }
    )
    FilterDialogs(
        uiState.dialogType,
        onAddRuleClick = { onEvent(FiltersContract.Event.OnAddFilterRule(it)) },
        onDeleteRuleClick = { onEvent(FiltersContract.Event.OnDeleteFilterRuleConfirmed(it)) },
        onDismissDialogClick = { onEvent(FiltersContract.Event.OnDismissDialog) }
    )
}
