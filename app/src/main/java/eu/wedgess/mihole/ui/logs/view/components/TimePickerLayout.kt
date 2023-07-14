package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.extensions.toDateString
import timber.log.Timber

@Composable
fun TimePickerLayout(
    fromTime: Long? = null,
    toTime: Long? = null,
    onFromTimeClicked: () -> Unit,
    onToTimeClicked: () -> Unit,
    onFromTimeCleared: () -> Unit,
    onToTimeCleared: () -> Unit
) {
    Timber.d("fromTime: $fromTime")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeButton(
            title = "From Time",
            subTitle = fromTime?.toDateString(),
            onClick = { onFromTimeClicked() },
            onClearClicked = { onFromTimeCleared() }
        )
        Text("-")
        TimeButton(
            title = "To Time",
            subTitle = toTime?.toDateString(),
            onClick = { onToTimeClicked() },
            onClearClicked = { onToTimeCleared() }
        )
    }
}

@Preview
@Composable
private fun TimePickerLayoutPreview() {
    MiHoleTheme {
        TimePickerLayout(
            onFromTimeClicked = { },
            onToTimeClicked = { },
            onFromTimeCleared = {},
            onToTimeCleared = {})
    }
}