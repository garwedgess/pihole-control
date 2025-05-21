package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ConnectionInputError
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ModifyConnectionContent(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_name),
            value = uiState.name,
            error = uiState.inputErrors.filterIsInstance<ConnectionInputError.Name>().firstOrNull(),
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnNameChanged(it)) }
        )

        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_host),
            value = uiState.host,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            error = uiState.inputErrors.filterIsInstance<ConnectionInputError.Host>().firstOrNull(),
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnHostChanged(it)) }
        )

        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_api_path),
            value = uiState.apiPath,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            ),
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnApiPathChanged(it)) }
        )

        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_port),
            value = uiState.port,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            error = uiState.inputErrors.filterIsInstance<ConnectionInputError.Port>().firstOrNull(),
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnPortChanged(it)) }
        )

        PasswordTextField(
            label = stringResource(R.string.modify_connection_label_password),
            value = uiState.password,
            inputVisible = uiState.passwordVisible,
            error = uiState.inputErrors
                .filterIsInstance<ConnectionInputError.Password>()
                .firstOrNull(),
            onVisibilityChanged = {
                onEvent(ModifyConnectionsContract.Event.OnPasswordFieldVisibilityChanged(it))
            },
            onValueChanged = {
                onEvent(ModifyConnectionsContract.Event.OnPasswordChanged(it))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (uiState.showAdvancedSettings) ImeAction.Next else ImeAction.Done
            )
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.modify_connection_label_show_advanced_settings))
            Switch(
                checked = uiState.showAdvancedSettings,
                onCheckedChange = {
                    onEvent(
                        ModifyConnectionsContract.Event.OnToggleAdvancedSettingsChanged(
                            it
                        )
                    )
                }
            )
        }
        AnimatedVisibility(visible = uiState.showAdvancedSettings) {
            ModifyConnectionAdvancedSettings(
                uiState = uiState,
                onEvent = onEvent
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onEvent(ModifyConnectionsContract.Event.SaveConnection)
            }
        ) {
            val resourceString = if (uiState.currentConnection == null) {
                R.string.modify_connection_btn_save
            } else {
                R.string.modify_connection_btn_update
            }
            Text(stringResource(resourceString))
        }
    }
}

@ThemePreview
@Composable
private fun ModifyConnectionContentPreview(
    @PreviewParameter(ModifyConnectionContentPreviewParams::class)
    params: ModifyConnectionsContract.UiState
) {
    PiHoleControlTheme {
        Surface {
            ModifyConnectionContent(params, onEvent = {})
        }
    }
}

private class ModifyConnectionContentPreviewParams :
    PreviewParameterProvider<ModifyConnectionsContract.UiState> {
    override val values: Sequence<ModifyConnectionsContract.UiState>
        get() = sequenceOf(
            ModifyConnectionsContract.UiState.initial(ConnectionEntity.default),
            ModifyConnectionsContract.UiState.initial(ConnectionEntity.default),
            ModifyConnectionsContract.UiState.initial(ConnectionEntity.default),
            with(ConnectionEntity.default) {
                ModifyConnectionsContract.UiState.initial(this).copy(currentConnection = this)
            },
            with(ConnectionEntity.default) {
                ModifyConnectionsContract.UiState.initial(this).copy(currentConnection = this)
            }
        )
}
