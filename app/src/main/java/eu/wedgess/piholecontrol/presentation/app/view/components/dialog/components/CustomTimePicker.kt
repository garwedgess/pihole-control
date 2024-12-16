package eu.wedgess.piholecontrol.presentation.app.view.components.dialog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun CustomTimePicker(
    currentHours: TextFieldValue,
    currentMinutes: TextFieldValue,
    onHoursChange: (hours: TextFieldValue) -> Unit,
    onMinutesChange: (minutes: TextFieldValue) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContentLarge)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Enter Custom Time",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.wrapContentHeight()) {
                Text(
                    text = "Hours",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal
                )
                TimeTextField(
                    type = TimePickerType.Hours,
                    value = currentHours,
                    onValueChange = {
                        onHoursChange(it)
                    }
                )
            }
            Box(Modifier.width(24.dp)) {
                Text(
                    ":",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Column {
                Text(
                    text = "Minutes",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal
                )
                TimeTextField(
                    type = TimePickerType.Minutes,
                    value = currentMinutes,
                    onValueChange = {
                        onMinutesChange(it)
                    }
                )
            }
        }
    }
}
