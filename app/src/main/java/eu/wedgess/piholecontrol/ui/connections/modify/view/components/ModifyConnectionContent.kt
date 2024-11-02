package eu.wedgess.piholecontrol.ui.connections.modify.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun ModifyConnectionContent(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit,
    paddingValues: PaddingValues
) {

    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.modify_connection_label_name)) },
            value = uiState.name,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnNameChanged(it)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.modify_connection_label_host)) },
            value = uiState.host,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnHostChanged(it)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.modify_connection_label_api_path)) },
            value = uiState.apiPath,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnApiPathChanged(it)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.modify_connection_label_port)) },
            value = uiState.port.toString(),
            onValueChange = {
                if (it.isDigitsOnly()) {
                    onEvent(ModifyConnectionsContract.Event.OnPortChanged(it.toInt()))
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        ApiTokenInput(
            apiToken = uiState.apiToken,
            showAdvancedSettings = uiState.showAdvancedSettings,
            onEvent = onEvent
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
                onEvent = onEvent,
                hideKeyboard = { keyboardController?.hide() }
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