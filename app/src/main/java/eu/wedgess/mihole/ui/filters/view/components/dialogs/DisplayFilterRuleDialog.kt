package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.utils.extensions.toBoolean
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayFilterRuleDialog(
    filterRule: PiHoleFilterRules.PiHoleFilterRule,
    onDismiss: () -> Unit,
    onDelete: (filterRule: PiHoleFilterRules.PiHoleFilterRule) -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getDateInstance() }

    AlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Filter Rule", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                }
            }
            FilterDetailsRow(
                icon = Icons.Default.Domain,
                title = "Domain",
                value = filterRule.domain
            )
            FilterDetailsRow(
                icon = Icons.Default.Rule,
                title = "Type",
                value = filterRule.type.name.lowercase().replaceFirstChar(Char::titlecase)
            )
            FilterDetailsRow(
                icon = Icons.Default.Schedule,
                title = "Date Added",
                value = dateTimeInstance.format(filterRule.dateAdded * 1000L)
            )
            FilterDetailsRow(
                icon = Icons.Default.Update,
                title = "Date Modified",
                value = dateTimeInstance.format(filterRule.dateModified * 1000L)
            )
            FilterDetailsRow(
                icon = if (filterRule.enabled.toBoolean()) Icons.Default.Check else Icons.Default.Close,
                title = "Status",
                value = if (filterRule.enabled.toBoolean()) "Enabled" else "Disabled"
            )
            FilterDetailsRow(
                icon = Icons.Default.Comment,
                title = "Comment",
                value = filterRule.comment.takeIf { it?.isNotBlank() == true } ?: "No comment"
            )

            Button(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), onClick = {
                onDelete(filterRule)
            }) {
                Text(text = "Delete Rule")
            }

        }
    }
}

@Composable
private fun FilterDetailsRow(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = title)
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(text = value, style = MaterialTheme.typography.bodySmall)
        }
    }
}