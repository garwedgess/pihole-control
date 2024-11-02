package eu.wedgess.piholecontrol.ui.settings.view.components

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
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.ui.common.previews.ThemePreview
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun ConnectionInfoContent(
    miHolesInfo: ConnectionInfo,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onSetActiveClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PiHoleControlTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        ConnectionInfoRow(
            icon = Icons.Default.Bolt,
            title = "Status",
            value = "Connected".takeIf { miHolesInfo.isActive } ?: "Disconnected"
        )
        ConnectionInfoRow(
            icon = Icons.Default.Api,
            title = "Api Path",
            value = "Connected".takeIf { miHolesInfo.isActive } ?: "Disconnected"
        )
        ConnectionInfoRow(
            icon = Icons.Default.Token,
            title = "Token",
            value = "Set".takeIf { miHolesInfo.token != null } ?: "Not Set"
        )
        ConnectionInfoRow(
            icon = Icons.Default.Lock,
            title = "Auth Credentials",
            value = "Set".takeIf { miHolesInfo.authUsername.isNotBlank() && miHolesInfo.authPassword.isNotBlank() }
                ?: "Not Set"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent,
                Alignment.End
            )
        ) {
            OutlinedButton(onClick = { onEditClicked() }) {
                Text(text = "Edit")
            }
            OutlinedButton(onClick = { onDeleteClicked() }) {
                Text(text = "Delete")
            }
            OutlinedButton(onClick = { onSetActiveClicked() }) {
                Text(text = "Set Active")
            }
        }
    }
}

@Composable
private fun ConnectionInfoRow(
    icon: ImageVector,
    title: String,
    value: String,
    valueTextColor: Color = Color.Unspecified
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
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = valueTextColor)
        }
    }
}


@ThemePreview
@Composable
private fun ConnectionInfoContentPreview() {
    PiHoleControlTheme {
        Surface {
            ConnectionInfoContent(
                miHolesInfo = ConnectionInfo.default,
                onEditClicked = {},
                onDeleteClicked = {},
                onSetActiveClicked = {}
            )
        }
    }
}