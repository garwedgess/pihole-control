package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.runtime.Composable
import eu.wedgess.mihole.data.model.responses.PiHoleFilterRules
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.model.FilterDialogType
import eu.wedgess.mihole.ui.filters.model.ModifyFilterRule
import eu.wedgess.mihole.ui.filters.view.components.dialogs.AddFilterRuleDialog
import eu.wedgess.mihole.ui.filters.view.components.dialogs.DisplayFilterRuleDetailsDialog

@Composable
fun FilterDialogs(
    dialogType: FilterDialogType,
    onAddRuleClick: (ModifyFilterRule.Add) -> Unit,
    onDeleteRuleClick: (ModifyFilterRule.Delete) -> Unit,
    onDismissDialogClick: () -> Unit
) {

    when (dialogType) {
        FilterDialogType.None -> Unit
        is FilterDialogType.AddFilterRule -> AddFilterRuleDialog(
            filterRuleType = dialogType.type,
            onDismissRequest = onDismissDialogClick,
            onConfirmClick = onAddRuleClick
        )

        is FilterDialogType.ShowFilterRuleInfo -> DisplayFilterRuleDetailsDialog(
            filterRule = dialogType.filterRule,
            onDismissRequest = onDismissDialogClick,
            onDelete = onDeleteRuleClick
        )

    }
}