package eu.wedgess.piholecontrol.presentation.settings.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> DropDownPreference(
    title: String,
    items: List<Pair<T, String>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    var dropDownExpanded by remember { mutableStateOf(value = false) }

    RegularPreference(
        title = title,
        subtitle = items.first { it.first == selectedItem }.second,
        icon = icon,
        onClick = {
            dropDownExpanded = true
        },
        modifier = modifier
            .background(
                color = if (dropDownExpanded)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    Color.Unspecified
            ),
        enabled = enabled,
    )

    Box {
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = dropDownExpanded,
            onDismissRequest = { dropDownExpanded = !dropDownExpanded },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        dropDownExpanded = false

                        onItemSelected(item.first)
                    },
                    modifier = Modifier
                        .background(
                            color = if (selectedItem == item.first)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            else
                                Color.Unspecified,
                        ),
                    text = {
                        Text(
                            text = item.second,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropDownPreferencePreview() {
    val themes = listOf(
        "system" to "System default",
        "light" to "Light",
        "dark" to "Dark",
    )
    var selectedTheme by remember { mutableStateOf(value = themes.first().first) }

    DropDownPreference(
        title = "Theme",
        icon = Icons.Default.Palette,
        items = themes,
        selectedItem = selectedTheme,
        onItemSelected = { },
    )
}
