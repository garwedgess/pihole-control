package eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.presentation.common.components.DropdownTextField
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.filters.extensions.isRegexFilterRuleType
import eu.wedgess.piholecontrol.presentation.filters.model.FilterRuleDraft
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun AddFilterRuleDialog(
    groups: List<GroupEntity>,
    draft: FilterRuleDraft,
    onDomainChange: (String) -> Unit,
    onGroupsChange: (Set<GroupEntity>) -> Unit,
    onCommentChange: (String) -> Unit,
    onRegexChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        FilterRuleDialogContent(
            title = stringResource(R.string.filters_add_rule_dialog_title),
            confirmText = stringResource(R.string.filters_add_rule_dialog_btn_add),
            groups = groups,
            domain = draft.domain,
            selectedGroups = draft.selectedGroups,
            comment = draft.comment,
            enabled = true,
            isRegex = draft.isRegex,
            canConfirm = draft.canConfirm,
            statusEditable = false,
            onDomainChange = onDomainChange,
            onGroupsChange = onGroupsChange,
            onCommentChange = onCommentChange,
            onEnabledChange = {},
            onRegexChange = onRegexChange,
            focusRequester = focusRequester,
            onConfirmClick = onConfirmClick,
            onCancelClick = onDismissRequest
        )
    }
}

@Composable
fun EditFilterRuleDialog(
    draft: FilterRuleInfo,
    groups: List<GroupEntity>,
    onDomainChange: (String) -> Unit,
    onGroupsChange: (Set<GroupEntity>) -> Unit,
    onCommentChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onRegexChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        FilterRuleDialogContent(
            title = stringResource(R.string.filters_edit_rule_dialog_title),
            confirmText = stringResource(R.string.filters_edit_rule_dialog_btn_update),
            groups = groups,
            domain = draft.domain,
            selectedGroups = groups.filter { it.id in draft.groups }.toSet(),
            comment = draft.comment.orEmpty(),
            enabled = draft.enabled,
            isRegex = draft.type.isRegexFilterRuleType(),
            canConfirm = draft.domain.isNotBlank(),
            statusEditable = true,
            onDomainChange = onDomainChange,
            onGroupsChange = onGroupsChange,
            onCommentChange = onCommentChange,
            onEnabledChange = onEnabledChange,
            onRegexChange = onRegexChange,
            focusRequester = focusRequester,
            onConfirmClick = onConfirmClick,
            onCancelClick = onDismissRequest
        )
    }
}

@Composable
private fun FilterRuleDialogContent(
    title: String,
    confirmText: String,
    groups: List<GroupEntity>,
    domain: String,
    selectedGroups: Set<GroupEntity>,
    comment: String,
    enabled: Boolean,
    isRegex: Boolean,
    canConfirm: Boolean,
    statusEditable: Boolean,
    onDomainChange: (String) -> Unit,
    onGroupsChange: (Set<GroupEntity>) -> Unit,
    onCommentChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onRegexChange: (Boolean) -> Unit,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadiusLarge)
    ) {
        Column(
            Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            )
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    modifier = Modifier.padding(
                        bottom = PiHoleControlTheme.dimens.padding.screenContent
                    ),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.filters_add_rule_dialog_label_domain)) },
                value = domain,
                maxLines = 1,
                onValueChange = onDomainChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            AnimatedVisibility(visible = groups.isNotEmpty()) {
                DropdownTextField(
                    selectedValues = selectedGroups,
                    options = groups,
                    label = "Group",
                    onValueChange = onGroupsChange,
                    valueFormatter = { it.name },
                    multiSelect = true
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.filters_details_dialog_label_comment)) },
                value = comment,
                maxLines = 1,
                onValueChange = onCommentChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            AnimatedVisibility(visible = statusEditable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContent,
                        Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.filters_edit_rule_dialog_label_enabled),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Checkbox(
                        checked = enabled,
                        onCheckedChange = onEnabledChange
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = PiHoleControlTheme.dimens.padding.dialogContent
                    ),
                horizontalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent,
                    Alignment.Start
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.filters_add_rule_dialog_label_as_regex),
                    style = MaterialTheme.typography.titleMedium
                )
                Checkbox(
                    checked = isRegex,
                    onCheckedChange = onRegexChange
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClick) {
                    Text(stringResource(R.string.all_btn_cancel))
                }
                TextButton(
                    enabled = canConfirm,
                    onClick = onConfirmClick
                ) {
                    Text(confirmText)
                }
            }
        }
    }
}

@ThemePreviewWithBackground
@Composable
private fun AddFilterRuleDialogPreview() {
    PiHoleControlTheme {
        Surface {
            AddFilterRuleDialog(
                groups = emptyList(),
                draft = FilterRuleDraft(),
                onDomainChange = {},
                onGroupsChange = {},
                onCommentChange = {},
                onRegexChange = {},
                onConfirmClick = { },
                onDismissRequest = { }
            )
        }
    }
}
