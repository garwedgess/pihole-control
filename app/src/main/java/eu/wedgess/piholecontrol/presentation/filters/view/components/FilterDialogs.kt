package eu.wedgess.piholecontrol.presentation.filters.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.presentation.common.components.AlertMessageDialog
import eu.wedgess.piholecontrol.presentation.filters.model.FilterDialogType
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs.AddFilterRuleDialog
import eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs.DisplayFilterRuleDetailsDialog
import eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs.EditFilterRuleDialog

@Composable
fun FilterDialogs(
    dialogType: FilterDialogType,
    onAddRuleClick: () -> Unit,
    onUpdateRuleClick: () -> Unit,
    onEditRuleClick: (FilterRuleInfo) -> Unit,
    onDomainChange: (String) -> Unit,
    onGroupsChange: (Set<GroupEntity>) -> Unit,
    onCommentChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onRegexChange: (Boolean) -> Unit,
    onDeleteRuleClick: (ModifyFilterRule.Delete) -> Unit,
    onDeleteSelectedRulesClick: () -> Unit,
    onDismissDialogClick: () -> Unit
) {
    when (dialogType) {
        FilterDialogType.None -> Unit
        is FilterDialogType.AddFilterRule -> AddFilterRuleDialog(
            groups = dialogType.groups,
            draft = dialogType.draft,
            onDomainChange = onDomainChange,
            onGroupsChange = onGroupsChange,
            onCommentChange = onCommentChange,
            onRegexChange = onRegexChange,
            onDismissRequest = onDismissDialogClick,
            onConfirmClick = onAddRuleClick
        )

        is FilterDialogType.EditFilterRule -> EditFilterRuleDialog(
            draft = dialogType.draft,
            groups = dialogType.groups,
            onDomainChange = onDomainChange,
            onGroupsChange = onGroupsChange,
            onCommentChange = onCommentChange,
            onEnabledChange = onEnabledChange,
            onRegexChange = onRegexChange,
            onDismissRequest = onDismissDialogClick,
            onConfirmClick = onUpdateRuleClick
        )

        is FilterDialogType.ShowFilterRuleInfo -> DisplayFilterRuleDetailsDialog(
            filterRule = dialogType.filterRule,
            onEdit = onEditRuleClick,
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

        is FilterDialogType.OnConfirmSelectedFiltersDelete -> AlertMessageDialog(
            titleText = stringResource(id = R.string.delete_selected_filters_dialog_title),
            messageText = stringResource(
                id = R.string.delete_selected_filters_dialog_message,
                dialogType.count
            ),
            confirmText = stringResource(id = R.string.delete_filter_dialog_confirm_btn),
            dismissText = stringResource(id = R.string.delete_filter_dialog_cancel_btn),
            onConfirm = onDeleteSelectedRulesClick,
            onDismiss = onDismissDialogClick
        )
    }
}
