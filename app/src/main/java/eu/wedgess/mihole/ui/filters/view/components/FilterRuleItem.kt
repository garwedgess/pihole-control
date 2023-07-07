package eu.wedgess.mihole.ui.filters.view.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleFilterRules
import eu.wedgess.mihole.data.model.enums.FilterRuleType
import java.text.DateFormat

@Composable
fun FilterRuleItem(
    rule: PiHoleFilterRules.PiHoleFilterRule,
    dateFormat: DateFormat,
    onItemClicked: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable { onItemClicked() },
        overlineContent = when (rule.type) {
            FilterRuleType.REGEX_BLACK, FilterRuleType.REGEX_WHITE -> ({ Text("RegExr") })
            else -> null
        },
        headlineContent = { Text(rule.domain) },
        supportingContent = rule.comment?.let { { Text(it) } },
        trailingContent = {
            Text(
                text = dateFormat.format(rule.dateAdded * 1000L)
            )
        })
}