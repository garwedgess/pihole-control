package eu.wedgess.piholecontrol.ui.connections.modify.view.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.connections.modify.ModifyConnectionsContract

@Composable
fun ApiTokenInput(
    apiToken: String,
    showAdvancedSettings: Boolean,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var apiKeyInputVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
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
        value = apiToken,
        onValueChange = { onEvent(ModifyConnectionsContract.Event.OnApiTokenChanged(it)) },
        visualTransformation = if (apiKeyInputVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = if (showAdvancedSettings) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }
        ),
        trailingIcon = {
            val image = if (apiKeyInputVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (apiKeyInputVisible) "Hide password" else "Show password"

            IconButton(onClick = { apiKeyInputVisible = !apiKeyInputVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}