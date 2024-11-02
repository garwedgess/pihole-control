package eu.wedgess.piholecontrol.ui.logs.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.extensions.toDateString

@Composable
fun TimePickerLayout(
    fromTime: Long? = null,
    toTime: Long? = null,
    onFromTimeClicked: () -> Unit,
    onToTimeClicked: () -> Unit,
    onFromTimeCleared: () -> Unit,
    onToTimeCleared: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeButton(
            title = stringResource(R.string.logs_filter_sheet_btn_from_time),
            subTitle = fromTime?.toDateString(),
            onClick = { onFromTimeClicked() },
            onClearClicked = { onFromTimeCleared() }
        )
        Text(stringResource(R.string.logs_filter_sheet_time_divider))
        TimeButton(
            title = stringResource(R.string.logs_filter_sheet_btn_to_time),
            subTitle = toTime?.toDateString(),
            onClick = { onToTimeClicked() },
            onClearClicked = { onToTimeCleared() }
        )
    }
}

@Preview
@Composable
private fun TimePickerLayoutPreview() {
    PiHoleControlTheme {
        TimePickerLayout(
            onFromTimeClicked = { },
            onToTimeClicked = { },
            onFromTimeCleared = {},
            onToTimeCleared = {})
    }
}