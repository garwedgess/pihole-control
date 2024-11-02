package eu.wedgess.piholecontrol.ui.filters.view.components.dialogs

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.enums.FilterRuleType
import eu.wedgess.piholecontrol.data.model.enums.WILDCARD_REGEX_PREFIX
import eu.wedgess.piholecontrol.data.model.enums.WILDCARD_REGEX_SUFFIX
import eu.wedgess.piholecontrol.ui.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun AddFilterRuleDialog(
    filterRuleType: FilterRuleType,
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
            focusRequester = focusRequester,
            onConfirmClick = onConfirmClick,
            onCancelClick = onDismissRequest
        )
    }
}

@Composable
private fun AddFilterRuleDialogContent(
    filterRuleType: FilterRuleType,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onConfirmClick: (ModifyFilterRule.Add) -> Unit,
    onCancelClick: () -> Unit
) {
    var isWildcardChecked by remember {
        mutableStateOf(false)
    }
    var value by remember {
        mutableStateOf("")
    }
    Surface(shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadius)) {
        Column(
            Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Text(
                    stringResource(R.string.filters_add_rule_dialog_title),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.filters_add_rule_dialog_label_domain)) },
                leadingIcon = if (isWildcardChecked) {
                    { Text(WILDCARD_REGEX_PREFIX) }
                } else null,
                value = value,
                trailingIcon = if (isWildcardChecked) {
                    { Text(WILDCARD_REGEX_SUFFIX) }
                } else null,
                onValueChange = {
                    value = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContent,
                    Alignment.Start
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.filters_add_rule_dialog_label_as_wildcard),
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
                TextButton(onClick = onCancelClick) { Text(stringResource(R.string.all_btn_cancel)) }
                TextButton(
                    onClick = {
                        val type = if (isWildcardChecked) {
                            if (filterRuleType == FilterRuleType.ALLOW) {
                                FilterRuleType.REGEX_ALLOW
                            } else {
                                FilterRuleType.REGEX_BLOCK
                            }
                        } else {
                            filterRuleType
                        }
                        onConfirmClick(ModifyFilterRule.Add(value, type))
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

@Composable
@Preview
fun AddFilterRuleCardPreview() {
    AddFilterRuleDialogContent(
        filterRuleType = FilterRuleType.ALLOW,
        focusRequester = FocusRequester(),
        onConfirmClick = { },
        onCancelClick = { }
    )
}