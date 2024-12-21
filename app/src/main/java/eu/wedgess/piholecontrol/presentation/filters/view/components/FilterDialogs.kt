package eu.wedgess.piholecontrol.presentation.filters.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.AlertMessageDialog
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

        is FilterDialogType.OnConfirmFilterDelete -> AlertMessageDialog(
            titleText = stringResource(id = R.string.delete_filter_dialog_title),
            messageText = stringResource(
                id = R.string.delete_filter_dialog_message,
                dialogType.filterRule.domain
            ),
            confirmText = stringResource(id = R.string.delete_filter_dialog_confirm_btn),
            dismissText = stringResource(id = R.string.delete_filter_dialog_cancel_btn),
            onConfirm = {
                onDeleteRuleClick(
                    ModifyFilterRule.Delete(
                        domain = dialogType.filterRule.domain,
                        type = dialogType.filterRule.type
                    )
                )
            },
            onDismiss = onDismissDialogClick
        )
    }
}
