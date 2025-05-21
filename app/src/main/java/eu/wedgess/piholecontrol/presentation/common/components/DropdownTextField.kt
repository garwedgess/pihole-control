package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownTextField(
    selectedValues: Set<T>,
    options: List<T>,
    label: String,
    onValueChange: (Set<T>) -> Unit,
    valueFormatter: (T) -> String,
    modifier: Modifier = Modifier,
    multiSelect: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValues.joinToString(", ") { valueFormatter(it) },
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                val isSelected = selectedValues.contains(option)
                DropdownMenuItem(
                    text = { Text(valueFormatter(option)) },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF119955)
                            )
                        }
                    } else null,
                    onClick = {
                        val updatedSelection = if (multiSelect) {
                            if (isSelected) selectedValues - option else selectedValues + option
                        } else {
                            setOf(option)
                        }
                        onValueChange(updatedSelection)

                        // Only close dropdown in single-select mode
                        if (!multiSelect) {
                            focusManager.clearFocus()
                            expanded = false
                        }
                    }
                )
            }
        }
    }
}
