package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import io.ktor.http.URLProtocol

@Composable
fun ModifyConnectionAdvancedSettings(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
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
        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_basic_auth_username),
            value = uiState.authUsername,
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnAuthUsernameChanged(it)) }
        )
        PasswordTextField(
            label = stringResource(R.string.modify_connection_label_basic_auth_password),
            value = uiState.authPassword,
            inputVisible = uiState.basicAuthPasswordVisible,
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnAuthPasswordChanged(it)) },
            onVisibilityChanged = {
                onEvent(ModifyConnectionsContract.Event.OnBasicPasswordFieldVisibilityChanged(it))
            }
        )
        ModifyConnectionTextField(
            label = stringResource(R.string.modify_connection_label_basic_auth_realm),
            value = uiState.authRealm,
            onValueChanged = { onEvent(ModifyConnectionsContract.Event.OnAuthUsernameChanged(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
    }
}
