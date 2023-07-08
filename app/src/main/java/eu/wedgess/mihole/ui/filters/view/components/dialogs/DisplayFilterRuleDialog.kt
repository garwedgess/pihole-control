package eu.wedgess.mihole.ui.filters.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.extensions.toBoolean
import java.text.DateFormat

@Composable
fun DisplayFilterRuleDialog(
    filterRule: PiHoleFilterRules.PiHoleFilterRule,
    onDelete: (filterRule: PiHoleFilterRules.PiHoleFilterRule) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        DisplayFilterRuleDialogContent(
            filterRule = filterRule,
            onDeleteClicked = { onDelete(filterRule) },
            onCancelClicked = { onDismissRequest() }
        )
    }
}

@Composable
private fun DisplayFilterRuleDialogContent(
    filterRule: PiHoleFilterRules.PiHoleFilterRule,
    onDeleteClicked: () -> Unit,
    onCancelClicked: () -> Unit
) {
    val dateTimeInstance = remember { DateFormat.getDateInstance() }

    Surface(shape = RoundedCornerShape(8.dp)) {
        Column(
            Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                Text("Filter rule", style = MaterialTheme.typography.titleLarge)
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

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClicked) { Text("CANCEL") }
                TextButton(onClick = onDeleteClicked) { Text("DELETE") }
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

@Preview
@Composable
private fun DisplayFilterRuleDialogPreview() {
    MiHoleTheme {
        DisplayFilterRuleDialog(
            filterRule = PiHoleFilterRules.PiHoleFilterRule(),
            onDelete = {},
            onDismissRequest = {}
        )
    }
}