package eu.wedgess.piholecontrol.presentation.logs.view.components.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogsTopBarActions(
    selectedSorting: LogSorting,
    isSortingMenuVisible: Boolean,
    onSearchClick: () -> Unit,
    onSortClick: () -> Unit,
    onFilterClick: () -> Unit,
    onDismissSort: () -> Unit,
    onSortItemClick: (LogSorting) -> Unit
) {
    Row {
        IconButton(onClick = onFilterClick) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(onClick = onSortClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Sort,
                contentDescription = "sort",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "search",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        DropdownMenu(expanded = isSortingMenuVisible, onDismissRequest = onDismissSort) {
            LogSorting.entries.forEach {
                DropdownMenuItem(
                    trailingIcon = {
                        RadioButton(
                            selected = it == selectedSorting,
                            onClick = null
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = it.icon, contentDescription = "")
                    },
                    text = { Text(text = it.uiText.asString()) },
                    onClick = { onSortItemClick(it) }
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun LogsTopAppBarActionsPreview() {
    PiHoleControlTheme {
        Surface {
            LogsTopBarActions(
                selectedSorting = LogSorting.DATE_ASC,
                isSortingMenuVisible = true,
                onSearchClick = {},
                onSortClick = {},
                onFilterClick = {},
                onDismissSort = {},
                onSortItemClick = {}
            )
        }
    }
}
