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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.presentation.common.components.DropdownTextField
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun AddFilterRuleDialog(
    filterRuleType: FilterRuleTypeEntity,
    groups: List<GroupEntity>,
    onDismissRequest: () -> Unit,
    onConfirmClick: (ModifyFilterRule.Add) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        AddFilterRuleDialogContent(
            filterRuleType = filterRuleType,
            groups = groups,
            focusRequester = focusRequester,
            onConfirmClick = onConfirmClick,
            onCancelClick = onDismissRequest
        )
    }
}

@Composable
private fun AddFilterRuleDialogContent(
    filterRuleType: FilterRuleTypeEntity,
    groups: List<GroupEntity>,
    onConfirmClick: (ModifyFilterRule.Add) -> Unit,
    onCancelClick: () -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    var isWildcardChecked by remember {
        mutableStateOf(false)
    }
    var domain by remember {
        mutableStateOf("")
    }
    var domainGroups by remember {
        mutableStateOf(setOfNotNull(groups.firstOrNull()))
    }
    var comment by remember {
        mutableStateOf("")
    }
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
                    text = stringResource(R.string.filters_add_rule_dialog_title),
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
                onValueChange = {
                    domain = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            AnimatedVisibility(visible = groups.isNotEmpty()) {
                DropdownTextField(
                    selectedValues = domainGroups,
                    options = groups,
                    label = "Group",
                    onValueChange = {
                        domainGroups = it
                    },
                    valueFormatter = { it.name },
                    multiSelect = true
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.filters_details_dialog_label_comment)) },
                value = comment,
                maxLines = 1,
                onValueChange = {
                    comment = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
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
                    checked = isWildcardChecked,
                    onCheckedChange = {
                        isWildcardChecked = it
                    }
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
                    onClick = {
                        val type = if (isWildcardChecked) {
                            if (filterRuleType == FilterRuleTypeEntity.ALLOW) {
                                FilterRuleTypeEntity.REGEX_ALLOW
                            } else {
                                FilterRuleTypeEntity.REGEX_DENY
                            }
                        } else {
                            filterRuleType
                        }
                        onConfirmClick(
                            ModifyFilterRule.Add(
                                domain = domain,
                                groups = domainGroups.map { it.id },
                                comment = comment,
                                type = type
                            )
                        )
                    }
                ) {
                    Text(
                        stringResource(R.string.filters_add_rule_dialog_btn_add)
                    )
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
                filterRuleType = FilterRuleTypeEntity.ALLOW,
                groups = emptyList(),
                onConfirmClick = { },
                onDismissRequest = { }
            )
        }
    }
}
