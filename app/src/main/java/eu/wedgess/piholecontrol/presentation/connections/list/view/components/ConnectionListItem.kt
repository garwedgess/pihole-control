package eu.wedgess.piholecontrol.presentation.connections.list.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.settings.view.components.ConnectionInfoContent
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun ConnectionListItem(
    connectionInfo: ConnectionEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSetActiveClick: () -> Unit
) {
    var expandMoreDropdown by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PiHoleControlTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.BottomEnd) {
                Icon(imageVector = Icons.Default.Dns, contentDescription = "")
                androidx.compose.animation.AnimatedVisibility(visible = connectionInfo.isActive) {
                    Surface(shape = RoundedCornerShape(percent = 50)) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "",
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.totalQueriesBackground
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = PiHoleControlTheme.dimens.padding.itemContent)
                    .weight(PiHoleControlTheme.dimens.weight.full)
            ) {
                Text(
                    text = "${connectionInfo.protocol.name}://${connectionInfo.host}:${connectionInfo.port}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = connectionInfo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                    )
                )
            }

            IconButton(onClick = { expandMoreDropdown = !expandMoreDropdown }) {
                Icon(
                    imageVector = if (expandMoreDropdown) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = ""
                )
            }
        }

        AnimatedVisibility(visible = expandMoreDropdown) {
            ConnectionInfoContent(
                connectionInfo,
                onEditClick = { onEditClick() },
                onDeleteClick = { onDeleteClick() },
                onSetActiveClick = { onSetActiveClick() }
            )
        }
    }
}

@ThemePreview
@Composable
private fun ConnectionListItemPreview() {
    PiHoleControlTheme {
        Surface {
            ConnectionListItem(
                ConnectionEntity.default,
                onEditClick = {},
                onSetActiveClick = {},
                onDeleteClick = {}
            )
        }
    }
}
