package eu.wedgess.piholecontrol.presentation.logs.view.components.actions

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.model.LogSorting
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogsTopBarActions(
    onSearchClick: () -> Unit
) {
    Row {
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(R.string.logs_cd_search),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun LogsOverflowMenuItems(
    selectedSorting: LogSorting,
    onFilterClick: () -> Unit,
    onSortItemClick: (LogSorting) -> Unit
) {
    DropdownMenuItem(
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null
            )
        },
        text = { Text(text = stringResource(R.string.logs_menu_filter)) },
        onClick = onFilterClick
    )
    HorizontalDivider()
    LogSorting.entries.forEach {
        DropdownMenuItem(
            trailingIcon = {
                RadioButton(
                    selected = it == selectedSorting,
                    onClick = null
                )
            },
            leadingIcon = {
                Icon(imageVector = it.icon, contentDescription = null)
            },
            text = { Text(text = it.uiText.asString()) },
            onClick = { onSortItemClick(it) }
        )
    }
}

@ThemePreview
@Composable
private fun LogsTopAppBarActionsPreview() {
    PiHoleControlTheme {
        Surface {
            LogsTopBarActions(
                onSearchClick = {}
            )
        }
    }
}
