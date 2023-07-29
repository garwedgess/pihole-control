package eu.wedgess.mihole.ui.app.view.components.dialog.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun TimeTextField(
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    allowedLength: Int = 2,
    defaultValue: String? = null
) {
    var focusState: FocusState? by rememberSaveable { mutableStateOf(null) }
    TextField(
        value,
        placeholder = {
            if (focusState?.isFocused == false) {
                Text(
                    defaultValue ?: "00",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center)
                )
            }
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
        onValueChange = {
            it.takeIf { it.length <= allowedLength && (it.toIntOrNull() ?: 0) > 0 }?.run { onValueChange(this) }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .width(MiHoleTheme.dimens.size.timeInputWidth)
            .onFocusChanged { focusState = it },
    )
}