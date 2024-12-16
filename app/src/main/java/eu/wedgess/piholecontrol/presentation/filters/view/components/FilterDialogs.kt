package eu.wedgess.piholecontrol.presentation.filters.view.components

import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs.AddFilterRuleDialog
import eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs.DisplayFilterRuleDetailsDialog

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
