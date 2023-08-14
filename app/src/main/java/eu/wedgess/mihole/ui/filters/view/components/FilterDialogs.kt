package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.components.dialogs.AddFilterRuleDialog
import eu.wedgess.mihole.ui.filters.view.components.dialogs.DisplayFilterRuleDetailsDialog

@Composable
fun FilterDialogs(
    selectedRule: PiHoleFilterRules.PiHoleFilterRule? = null,
    displayAddRuleDialog: Boolean,
    onEvent: (FiltersContract.Event) -> Unit
) {
    if (selectedRule != null) {
        DisplayFilterRuleDetailsDialog(
            filterRule = selectedRule,
            onDismissRequest = {
                onEvent(FiltersContract.Event.OnRuleDeselected)
            },
            onDelete = {
                onEvent(FiltersContract.Event.RemoveRule(it.domain, it.type))
            }
        )
    }
    AnimatedVisibility(visible = displayAddRuleDialog) {
        AddFilterRuleDialog(
            onDismissRequest = { onEvent(FiltersContract.Event.OnDismissAddRuleDialog) },
            onConfirmClick = { rule, isWildCard ->
                onEvent(FiltersContract.Event.AddRule(rule = rule, isRegex = isWildCard))
            }
        )
    }
}