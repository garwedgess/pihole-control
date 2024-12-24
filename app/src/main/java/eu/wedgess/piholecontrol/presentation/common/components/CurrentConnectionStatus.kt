package eu.wedgess.piholecontrol.presentation.common.components

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
import androidx.compose.material3.ListItemDefaults
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
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.adBlockingDisabled
import eu.wedgess.piholecontrol.presentation.theme.adBlockingEnabled

@Composable
fun CurrentConnectionStatus(
    modifier: Modifier,
    currentConnection: ConnectionEntity,
    adBlockingEnabled: Boolean,
    connections: List<ConnectionEntity>,
    onConnectionClick: (ConnectionEntity) -> Unit,
    onStatusClick: () -> Unit
) {
    var showConnectionsDropdown by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { onStatusClick() }) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = if (adBlockingEnabled) Icons.Default.GppGood else Icons.Default.GppBad,
                tint = if (adBlockingEnabled) {
                    MaterialTheme.colorScheme.adBlockingEnabled
                } else {
                    MaterialTheme.colorScheme.adBlockingDisabled
                },
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
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                    ),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
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
                                colors = ListItemDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                ),
                                leadingContent = {
                                    RadioButton(
                                        selected = connection.id == currentConnection.id,
                                        onClick = null
                                    )
                                },
                                headlineContent = { Text(connection.name) },
                                supportingContent = { Text(connection.host) }
                            )
                        },
                        onClick = {
                            onConnectionClick(connection)
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
    PiHoleControlTheme {
        CurrentConnectionStatus(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            currentConnection = ConnectionEntity.default,
            connections = listOf(ConnectionEntity.default),
            adBlockingEnabled = true,
            onConnectionClick = {},
            onStatusClick = {}
        )
    }
}
