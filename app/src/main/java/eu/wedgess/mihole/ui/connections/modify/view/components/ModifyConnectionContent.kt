package eu.wedgess.mihole.ui.connections.modify.view.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.connections.modify.ModifyConnectionsContract
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ModifyConnectionContent(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit,
    paddingValues: PaddingValues
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var apiPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState)
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
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
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(R.string.modify_connection_label_api_token)) },
            leadingIcon = {
                IconButton(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(Manifest.permission.CAMERA),
                            0
                        )
                    }
                    onEvent(ModifyConnectionsContract.Event.OnOpenBarcodeScanner)
                }) {
                    Icon(
                        Icons.Default.QrCode,
                        contentDescription = "Scanner"
                    )
                }
            },
            value = uiState.apiToken,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnApiTokenChanged(it)) },
            visualTransformation = if (apiPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = if (uiState.showAdvancedSettings) {
                    ImeAction.Next
                } else {
                    ImeAction.Done
                }
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            trailingIcon = {
                val image = if (apiPasswordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (apiPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = { apiPasswordVisible = !apiPasswordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.modify_connection_label_show_advanced_settings))
            Switch(
                checked = uiState.showAdvancedSettings,
                onCheckedChange = { onEvent(ModifyConnectionsContract.Event.OnShowAdvancedSettingsChanged(it)) }
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
            val resourceString = if (uiState.currentConnection is UiResult.Success) {
                R.string.modify_connection_btn_update
            } else {
                R.string.modify_connection_btn_save
            }
            Text(stringResource(resourceString))
        }
    }
}