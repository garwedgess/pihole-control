@file:JvmName("PasswordInputKt")

package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.UiText

@Composable
fun PasswordTextField(
    label: String,
    value: String,
    inputVisible: Boolean,
    onValueChanged: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next
    ),
    leadingIcon: @Composable (() -> Unit)? = null,
    error: ConnectionInputError? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        leadingIcon = leadingIcon,
        value = value,
        onValueChange = onValueChanged,
        visualTransformation = if (inputVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        trailingIcon = {
            val image = if (inputVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = { onVisibilityChanged(!inputVisible) }) {
                Icon(
                    imageVector = image,
                    contentDescription = if (inputVisible) "Hide" else "Show")
            }
        },
        supportingText = {
            error?.run {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = this.error.asString(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@ThemePreview
@Composable
private fun PasswordTextFieldPreview(
    @PreviewParameter(PasswordTextFieldPreviewParams::class) params: PasswordTextFieldParams
) {
    PiHoleControlTheme {
        Surface {
            PasswordTextField(
                label = params.label,
                value = params.value,
                inputVisible = params.visible,
                error = params.error,
                leadingIcon = params.leadingIcon,
                onVisibilityChanged = {},
                onValueChanged = {}
            )
        }
    }
}

private data class PasswordTextFieldParams(
    val label: String,
    val value: String,
    val visible: Boolean,
    val error: ConnectionInputError?,
    val leadingIcon: @Composable () -> Unit = {}
)

private class PasswordTextFieldPreviewParams : PreviewParameterProvider<PasswordTextFieldParams> {
    override val values: Sequence<PasswordTextFieldParams>
        get() = sequenceOf(
            PasswordTextFieldParams(
                label = "Api Key",
                value = "12345678910111213141516",
                visible = false,
                error = null,
                leadingIcon = {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "Scanner"
                    )
                }
            ),
            PasswordTextFieldParams(
                label = "Api Key",
                value = "12345678910111213141516",
                visible = true,
                error = null,
                leadingIcon = {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "Scanner"
                    )
                }
            ),
            PasswordTextFieldParams(
                label = "Api Key",
                value = "",
                visible = false,
                error = ConnectionInputError.ApiKey(UiText.DynamicString("ApiKey cannot be blank")),
                leadingIcon = {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "Scanner"
                    )
                }
            ),
            PasswordTextFieldParams(
                label = "Password",
                value = "password",
                visible = false,
                error = null,
            ),
            PasswordTextFieldParams(
                label = "Password",
                value = "password",
                visible = true,
                error = null
            ),
            PasswordTextFieldParams(
                label = "Password",
                value = "password",
                visible = false,
                error = ConnectionInputError.Password(UiText.DynamicString("Password cannot be blank")),
            )
        )

}
