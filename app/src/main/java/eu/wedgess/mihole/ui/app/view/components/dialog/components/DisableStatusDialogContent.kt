package eu.wedgess.mihole.ui.app.view.components.dialog.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
        mutableStateOf("00")
    }
    var customTimeMinutes by remember {
        mutableStateOf("00")
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { disableTimeMillis = TimeUnit.SECONDS.toMillis(30) }) {
                Text(text = stringResource(id = R.string.status_disable_btn_thirty_seconds))
            }
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { disableTimeMillis = TimeUnit.MINUTES.toMillis(1) }) {
                Text(text = stringResource(id = R.string.status_disable_btn_one_minute))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { disableTimeMillis = TimeUnit.MINUTES.toMillis(2) }) {
                Text(text = stringResource(id = R.string.status_disable_btn_two_minutes))
            }
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { disableTimeMillis = TimeUnit.MINUTES.toMillis(5) }) {
                Text(text = stringResource(id = R.string.status_disable_btn_five_minutes))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { disableTimeMillis = Long.MAX_VALUE }) {
                Text(text = stringResource(id = R.string.status_disable_btn_indefinite))
            }
            OutlinedButton(
                modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
                onClick = { showCustomTimeInput = !showCustomTimeInput }) {
                Text(text = stringResource(id = R.string.status_disable_btn_custom))
            }
        }

        AnimatedVisibility(visible = showCustomTimeInput) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContentLarge)
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
                            value = customTimeHours,
                            onValueChange = {
                                customTimeHours = it
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
                            value = customTimeMinutes,
                            onValueChange = {
                                customTimeMinutes = it
                            }
                        )
                    }
                }
            }
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
                val disableDuration = if (customTimeHours != "00" || customTimeMinutes != "00") {
                    TimeUnit.HOURS.toMillis(customTimeHours.toLong()).plus(
                        TimeUnit.MINUTES.toMillis(customTimeMinutes.toLong())
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