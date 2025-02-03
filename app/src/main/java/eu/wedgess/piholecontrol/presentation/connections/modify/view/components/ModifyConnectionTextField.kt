package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError

@Composable
fun ModifyConnectionTextField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    error: ConnectionInputError? = null
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChanged,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        supportingText = {
            error?.run {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = this.error.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (error != null) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
