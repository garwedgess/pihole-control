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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun TimeTextField(
    type: TimePickerType,
    value: TextFieldValue,
    onValueChange: (value: TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    defaultValue: String? = null
) {
    var focusState: FocusState? by rememberSaveable { mutableStateOf(null) }
    TextField(
        value = value,
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
        onValueChange = { time ->
            timeInputOnChange(
                value = time,
                prevValue = value,
                max = type.max,
                onNewValue = onValueChange
            )
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

private fun timeInputOnChange(
    value: TextFieldValue,
    prevValue: TextFieldValue,
    max: Int,
    onNewValue: (value: TextFieldValue) -> Unit
) {
    if (value.text == prevValue.text) {
        // just selection change
        onNewValue(value)
        return
    }

    if (value.text.isEmpty()) {
        onNewValue(value.copy(text = ""))
        return
    }

    try {
        val newValue = if (value.text.length == 3 && value.selection.start == 1) {
            value.text[0].digitToInt()
        } else {
            value.text.toInt()
        }

        if (newValue <= max) {
            onNewValue(
                if (value.text.length <= 2) {
                    value.copy(text = value.text)
                } else {
                    value.copy(text = value.text[0].toString())
                }
            )
        }
    } catch (_: NumberFormatException) {
    } catch (_: IllegalArgumentException) {
        // do nothing no state update
    }
}

enum class TimePickerType(val max: Int) {
    Hours(99), Minutes(60), Seconds(60)
}