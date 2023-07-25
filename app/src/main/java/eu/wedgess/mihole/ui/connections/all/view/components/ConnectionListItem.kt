package eu.wedgess.mihole.ui.connections.all.view.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.settings.view.components.ConnectionInfoContent
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.totalQueriesBackground

@Composable
fun ConnectionListItem(
    miHolesInfo: MiHolesInfo,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onSetActiveClicked: () -> Unit
) {

    var expandMoreDropdown by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MiHoleTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp), contentAlignment = Alignment.BottomEnd) {
                Icon(imageVector = Icons.Default.Dns, contentDescription = "")
                androidx.compose.animation.AnimatedVisibility(visible = miHolesInfo.isActive) {
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
                    .padding(start = MiHoleTheme.dimens.padding.itemContent)
                    .weight(MiHoleTheme.dimens.weight.full)
            ) {
                Text(
                    text = "${miHolesInfo.protocol.name}://${miHolesInfo.host}:${miHolesInfo.port}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = miHolesInfo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha)
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
                miHolesInfo,
                onEditClicked = { onEditClicked() },
                onDeleteClicked = { onDeleteClicked() },
                onSetActiveClicked = { onSetActiveClicked() }
            )
        }
    }
}


@ThemePreview
@Composable
fun ConnectionListItemPreview() {
    MiHoleTheme {
        Surface {
            ConnectionListItem(
                MiHolesInfo.default,
                onEditClicked = {},
                onSetActiveClicked = {},
                onDeleteClicked = {}
            )
        }
    }
}