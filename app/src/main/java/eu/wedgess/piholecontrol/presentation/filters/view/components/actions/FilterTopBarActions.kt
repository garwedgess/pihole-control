package eu.wedgess.piholecontrol.presentation.filters.view.components.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption

@Composable
fun FilterTopBarActions(
    onSearchClick: () -> Unit,
    onFilterByClick: () -> Unit,
    isFilterByMenuVisible: Boolean,
    selectedFilterByOptions: List<FilterByOption>,
    availableFilterByOptions: List<FilterByOption>,
    onFilterByOptionSelected: (FilterByOption) -> Unit,
    onDismissFiltering: () -> Unit
) {
    Row {
        IconButton(onClick = onFilterByClick) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            expanded = isFilterByMenuVisible,
            onDismissRequest = { onDismissFiltering() }
        ) {
            availableFilterByOptions.forEach { option ->
                DropdownMenuItem(
                    trailingIcon = {
                        RadioButton(
                            selected = selectedFilterByOptions.contains(option),
                            onClick = null
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = option.icon, contentDescription = "")
                    },
                    text = { Text(text = option.label.asString()) },
                    onClick = { onFilterByOptionSelected(option) }
                )
            }
        }
    }
}
