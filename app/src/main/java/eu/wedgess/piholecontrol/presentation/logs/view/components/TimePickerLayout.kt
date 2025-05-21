package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.extensions.toDateString

@Composable
fun TimePickerLayout(
    onFromTimeClick: () -> Unit,
    onToTimeClick: () -> Unit,
    onClearFromTime: () -> Unit,
    onClearToTime: () -> Unit,
    fromTime: Long? = null,
    toTime: Long? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeButton(
            title = stringResource(R.string.logs_filter_sheet_btn_from_time),
            subTitle = fromTime?.toDateString(),
            onClick = { onFromTimeClick() },
            onClearClick = { onClearFromTime() }
        )
        Text(stringResource(R.string.logs_filter_sheet_time_divider))
        TimeButton(
            title = stringResource(R.string.logs_filter_sheet_btn_to_time),
            subTitle = toTime?.toDateString(),
            onClick = { onToTimeClick() },
            onClearClick = { onClearToTime() }
        )
    }
}

@ThemePreview
@Composable
private fun TimePickerLayoutPreview() {
    PiHoleControlTheme {
        Surface {
            TimePickerLayout(
                onFromTimeClick = { },
                onToTimeClick = { },
                onClearFromTime = {},
                onClearToTime = {}
            )
        }
    }
}
