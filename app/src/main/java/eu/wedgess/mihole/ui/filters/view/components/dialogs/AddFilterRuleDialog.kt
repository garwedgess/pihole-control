package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.wedgess.mihole.data.model.enums.WILDCARD_REGEX_PREFIX
import eu.wedgess.mihole.data.model.enums.WILDCARD_REGEX_SUFFIX

@Composable
fun AddFilterRuleDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: (rule: String, isWildCard: Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismissRequest) {
        AddFilterRuleCard(
            focusRequester = focusRequester,
            onConfirmClick = onConfirmClick,
            onCancelClick = onDismissRequest
        )
    }
}

@Composable
fun AddFilterRuleCard(
    focusRequester: FocusRequester = remember { FocusRequester() },
    onConfirmClick: (rule: String, isWildCard: Boolean) -> Unit,
    onCancelClick: () -> Unit
) {
    var isWildcardChecked by remember {
        mutableStateOf(false)
    }
    var value by remember {
        mutableStateOf("")
    }
    Card {
        Column {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                    Text("Add rule", style = MaterialTheme.typography.titleMedium)
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    label = { Text("Domain") },
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
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Add as wildcard")
                    Switch(
                        checked = isWildcardChecked,
                        onCheckedChange = {
                            isWildcardChecked = it
                        }
                    )
                }
            }
            Divider()
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClick) { Text("CANCEL") }
                TextButton(onClick = { onConfirmClick(value, isWildcardChecked) }) { Text("ADD") }
            }
        }
    }
}

@Composable
@Preview
fun AddFilterRuleCardPreview() {
    AddFilterRuleCard(
        onConfirmClick = { _, _ -> },
        onCancelClick = {})
}