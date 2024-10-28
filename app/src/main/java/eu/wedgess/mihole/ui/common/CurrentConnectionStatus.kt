package eu.wedgess.mihole.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.adBlockingDisabled
import eu.wedgess.mihole.ui.theme.adBlockingEnabled

@Composable
fun CurrentConnectionStatus(
    modifier: Modifier,
    currentConnection: PiHoleInfo,
    adBlockingEnabled: Boolean,
    connections: List<PiHoleInfo>,
    onConnectionSelected: (PiHoleInfo) -> Unit,
    onStatusClicked: () -> Unit
) {
    var showConnectionsDropdown by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { onStatusClicked() }) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = if (adBlockingEnabled) Icons.Default.GppGood else Icons.Default.GppBad,
                tint = if (adBlockingEnabled) MaterialTheme.colorScheme.adBlockingEnabled else MaterialTheme.colorScheme.adBlockingDisabled,
                contentDescription = ""
            )
        }

        Box {
            Column(
                modifier = Modifier.clickable { showConnectionsDropdown = true },
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = currentConnection.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = currentConnection.host,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                )
            }
            DropdownMenu(
                expanded = showConnectionsDropdown,
                onDismissRequest = { showConnectionsDropdown = false }
            ) {

                connections.forEach { connection ->
                    DropdownMenuItem(
                        text = {
                            ListItem(
                                leadingContent = {
                                    RadioButton(
                                        selected = connection.id == currentConnection.id,
                                        onClick = null
                                    )
                                },
                                headlineContent = { Text(connection.name) },
                                supportingContent = { Text(connection.host) }
                            )
                        }, onClick = {
                            onConnectionSelected(connection)
                            showConnectionsDropdown = false
                        }
                    )
                }

            }
        }
    }
}


@ThemePreview
@Composable
private fun CurrentConnectionStatusPreview() {
    MiHoleTheme {
        CurrentConnectionStatus(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            currentConnection = PiHoleInfo.default,
            connections = listOf(PiHoleInfo.default),
            adBlockingEnabled = true,
            onConnectionSelected = {},
            onStatusClicked = {}
        )
    }
}