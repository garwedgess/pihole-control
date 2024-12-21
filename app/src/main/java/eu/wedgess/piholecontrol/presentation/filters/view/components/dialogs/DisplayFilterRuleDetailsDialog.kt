package eu.wedgess.piholecontrol.presentation.filters.view.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Domain
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreviewWithBackground
import eu.wedgess.piholecontrol.presentation.filters.model.ModifyFilterRule
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRuleInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun DisplayFilterRuleDetailsDialog(
    filterRule: FilterRuleInfo,
    onDelete: (ModifyFilterRule.Delete) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        DisplayFilterRuleDetailsDialogContent(
            filterRule = filterRule,
            onDeleteClick = onDelete,
            onCancelClick = { onDismissRequest() }
        )
    }
}

@Composable
private fun DisplayFilterRuleDetailsDialogContent(
    filterRule: FilterRuleInfo,
    onDeleteClick: (ModifyFilterRule.Delete) -> Unit,
    onCancelClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadiusLarge)
    ) {
        Column(
            Modifier.padding(PiHoleControlTheme.dimens.padding.dialogContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            )
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                Text(
                    modifier = Modifier.padding(
                        bottom = PiHoleControlTheme.dimens.padding.screenContent
                    ),
                    text = stringResource(R.string.filters_details_dialog_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            FilterDetailsRow(
                icon = Icons.Default.Domain,
                title = stringResource(R.string.filters_details_dialog_label_domain),
                value = filterRule.domain
            )
            FilterDetailsRow(
                icon = Icons.AutoMirrored.Default.Rule,
                title = stringResource(R.string.filters_details_dialog_label_type),
                value = filterRule.typeTitle
            )
            FilterDetailsRow(
                icon = Icons.Default.Schedule,
                title = stringResource(R.string.filters_details_dialog_label_date_added),
                value = filterRule.dateAdded
            )
            FilterDetailsRow(
                icon = Icons.Default.Update,
                title = stringResource(R.string.filters_details_dialog_label_date_modified),
                value = filterRule.dateModified
            )
            FilterDetailsRow(
                icon = if (filterRule.enabled) Icons.Default.Check else Icons.Default.Close,
                title = stringResource(R.string.filters_details_dialog_label_status),
                value = if (filterRule.enabled) {
                    stringResource(R.string.filters_details_dialog_value_enabled)
                } else {
                    stringResource(R.string.filters_details_dialog_value_disabled)
                }
            )
            FilterDetailsRow(
                icon = Icons.AutoMirrored.Default.Comment,
                title = stringResource(R.string.filters_details_dialog_label_comment),
                value = filterRule.comment.takeIf { it?.isNotBlank() == true }
                    ?: stringResource(R.string.filters_details_dialog_value_no_comment)
            )

            Row(
                Modifier.fillMaxWidth().padding(
                    top = PiHoleControlTheme.dimens.padding.dialogContent
                ),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancelClick) { Text(stringResource(id = R.string.all_btn_cancel)) }
                TextButton(onClick = {
                    onDeleteClick(
                        ModifyFilterRule.Delete(
                            filterRule.domain,
                            filterRule.type
                        )
                    )
                }) { Text("Delete") }
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
        horizontalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.screenContent
        )
    ) {
        Icon(
            modifier = Modifier.size(PiHoleControlTheme.dimens.size.defaultIcon),
            imageVector = icon,
            contentDescription = title
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                    )
                )
            )
        }
    }
}

@ThemePreviewWithBackground
@Composable
private fun DisplayFilterRuleDetailsDialogPreview() {
    PiHoleControlTheme {
        DisplayFilterRuleDetailsDialog(
            filterRule = FilterRuleInfo(
                id = 1,
                enabled = true,
                comment = "Some random comment",
                dateAdded = "14-01-2022",
                dateModified = "14-01-2023",
                domain = "google.com",
                groups = emptyList(),
                type = FilterRuleTypeEntity.ALLOW
            ),
            onDelete = {},
            onDismissRequest = {}
        )
    }
}
