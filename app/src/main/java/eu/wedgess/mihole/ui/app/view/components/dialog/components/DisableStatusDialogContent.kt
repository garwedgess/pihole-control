package eu.wedgess.mihole.ui.app.view.components.dialog.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import java.util.concurrent.TimeUnit

@Composable
fun DisableStatusDialogContent(
    onDisableStatus: (duration: Long) -> Unit,
    onDismiss: () -> Unit
) {
    var showCustomTimeInput by remember {
        mutableStateOf(false)
    }
    var disableTimeMillis by remember {
        mutableLongStateOf(0L)
    }
    var customTimeHours by remember {
        mutableStateOf(TextFieldValue())
    }
    var customTimeMinutes by remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        modifier = Modifier.padding(MiHoleTheme.dimens.padding.dialogContent),
        verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MiHoleTheme.dimens.padding.itemContentLarge),
            text = "Disable",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        DisableButtonRow(
            selectedTime = disableTimeMillis,
            leftButtonTextResId = R.string.status_disable_btn_thirty_seconds,
            leftValue = TimeUnit.SECONDS.toMillis(30),
            rightButtonTextResId = R.string.status_disable_btn_one_minute,
            rightValue = TimeUnit.MINUTES.toMillis(1),
            onTimeSelected = {
                if (showCustomTimeInput) showCustomTimeInput = false
                disableTimeMillis = it
            }
        )
        DisableButtonRow(
            selectedTime = disableTimeMillis,
            leftButtonTextResId = R.string.status_disable_btn_two_minutes,
            leftValue = TimeUnit.MINUTES.toMillis(2),
            rightButtonTextResId = R.string.status_disable_btn_five_minutes,
            rightValue = TimeUnit.MINUTES.toMillis(5),
            onTimeSelected = {
                if (showCustomTimeInput) showCustomTimeInput = false
                disableTimeMillis = it
            }
        )
        DisableButtonRow(
            selectedTime = disableTimeMillis,
            leftButtonTextResId = R.string.status_disable_btn_indefinite,
            leftValue = Long.MAX_VALUE,
            rightButtonTextResId = R.string.status_disable_btn_custom,
            rightValue = Long.MIN_VALUE,
            onTimeSelected = {
                if (it == Long.MIN_VALUE) {
                    showCustomTimeInput = true
                    disableTimeMillis = Long.MIN_VALUE
                } else {
                    if (showCustomTimeInput) showCustomTimeInput = false
                    disableTimeMillis = it
                }
            }
        )

        AnimatedVisibility(visible = showCustomTimeInput) {
            CustomTimePicker(
                currentHours = customTimeHours,
                currentMinutes = customTimeMinutes,
                onHoursChanged = { customTimeHours = it },
                onMinutesChanged = { customTimeMinutes = it }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MiHoleTheme.dimens.padding.itemContentXLarge),
            horizontalArrangement = Arrangement.spacedBy(
                MiHoleTheme.dimens.padding.itemContent,
                Alignment.End
            )
        ) {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
            TextButton(onClick = {
                val disableDuration = if (customTimeHours.text.isNotBlank() || customTimeMinutes.text.isNotBlank()) {
                    TimeUnit.HOURS.toMillis(customTimeHours.text.ifBlank { "00" }.toLong()).plus(
                        TimeUnit.MINUTES.toMillis(customTimeMinutes.text.ifBlank { "00" }.toLong())
                    )
                } else {
                    disableTimeMillis
                }
                onDisableStatus(disableDuration)
            }) {
                Text(text = "Confirm")
            }
        }
    }
}

@ThemePreview
@Composable
private fun DisableStatusDialogContentPreview() {
    MiHoleTheme {
        Surface {
            DisableStatusDialogContent(onDisableStatus = {}, onDismiss = {})
        }
    }
}