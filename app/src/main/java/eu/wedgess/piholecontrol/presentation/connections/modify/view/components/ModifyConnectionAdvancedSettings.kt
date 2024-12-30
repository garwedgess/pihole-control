package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import io.ktor.http.URLProtocol

@Composable
fun ModifyConnectionAdvancedSettings(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit,
    hideKeyboard: () -> Unit
) {
    var authPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.modify_connection_label_protocol))
            Switch(
                checked = uiState.protocol == URLProtocol.HTTPS,
                onCheckedChange = {
                    val protocol = if (it) URLProtocol.HTTPS else URLProtocol.HTTP
                    onEvent(ModifyConnectionsContract.Event.OnProtocolChanged(protocol))
                }
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.modify_connection_label_trust_all_certificates))
            Switch(
                checked = uiState.trustAllCerts,
                onCheckedChange = {
                    onEvent(
                        ModifyConnectionsContract.Event.OnTrustAllCertsChanged(it)
                    )
                }
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.modify_connection_label_basic_auth_username)) },
            value = uiState.authUsername,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnAuthUsernameChanged(it)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.modify_connection_label_basic_auth_password)) },
            value = uiState.authPassword,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnAuthPasswordChanged(it)) },
            visualTransformation = if (authPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                val image = if (authPasswordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }

                val description = if (authPasswordVisible) "Hide password" else "Show password"

                IconButton(onClick = { authPasswordVisible = !authPasswordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(stringResource(R.string.modify_connection_label_basic_auth_realm)) },
            value = uiState.authRealm,
            onValueChange = { onEvent(ModifyConnectionsContract.Event.OnAuthRealmChanged(it)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { hideKeyboard() }
            )
        )
    }
}
