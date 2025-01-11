package eu.wedgess.piholecontrol.presentation.settings.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ConnectionInfoContent(
    connection: ConnectionEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSetActiveClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentSmall
        )
    ) {
        ConnectionInfoRow(
            icon = Icons.Default.Bolt,
            title = "Status",
            value = "Connected".takeIf { connection.isActive } ?: "Disconnected"
        )
        if (connection is ConnectionEntity.Version5) {
            ConnectionInfoRow(
                icon = Icons.Default.Api,
                title = "Api Path",
                value = connection.apiPath
            )
        }
        if (connection is ConnectionEntity.Version5) {
            ConnectionInfoRow(
                icon = Icons.Default.Token,
                title = "Token",
                value = "Set".takeIf { connection.token.isNotBlank() } ?: "Not Set"
            )
        }

        if (connection is ConnectionEntity.Version6) {
            ConnectionInfoRow(
                icon = Icons.Default.Token,
                title = "Password",
                value = "Set".takeIf { connection.password.isNotBlank() } ?: "Not Set"
            )
        }
        ConnectionInfoRow(
            icon = Icons.Default.Lock,
            title = "Auth Credentials",
            value = "Set".takeIf { connection.authUsername.isNotBlank() && connection.authPassword.isNotBlank() }
                ?: "Not Set"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent,
                Alignment.End
            )
        ) {
            OutlinedButton(onClick = { onEditClick() }) {
                Text(text = "Edit")
            }
            OutlinedButton(onClick = { onDeleteClick() }) {
                Text(text = "Delete")
            }
            AnimatedVisibility(visible = !connection.isActive) {
                OutlinedButton(onClick = { onSetActiveClick() }) {
                    Text(text = "Set Active")
                }
            }
        }
    }
}

@Composable
private fun ConnectionInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
    valueTextColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Icon(imageVector = icon, contentDescription = title)
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = valueTextColor.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        }
    }
}

@ThemePreview
@Composable
private fun ConnectionInfoContentPreview() {
    PiHoleControlTheme {
        Surface {
            ConnectionInfoContent(
                connection = ConnectionEntity.Version5.default,
                onEditClick = {},
                onDeleteClick = {},
                onSetActiveClick = {}
            )
        }
    }
}
