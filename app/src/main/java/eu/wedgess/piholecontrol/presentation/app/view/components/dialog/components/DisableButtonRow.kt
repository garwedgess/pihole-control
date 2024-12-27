package eu.wedgess.piholecontrol.presentation.app.view.components.dialog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun DisableButtonRow(
    selectedTime: Long,
    leftButtonTextResId: Int,
    leftValue: Long,
    rightButtonTextResId: Int,
    rightValue: Long,
    onTimeClick: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        OutlinedButton(
            modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.full),
            border = if (selectedTime == leftValue) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                ButtonDefaults.outlinedButtonBorder(enabled = true)
            },
            onClick = { onTimeClick(leftValue) }
        ) {
            Text(text = stringResource(id = leftButtonTextResId))
        }
        OutlinedButton(
            modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.full),
            border = if (selectedTime == rightValue) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                ButtonDefaults.outlinedButtonBorder(enabled = true)
            },
            onClick = { onTimeClick(rightValue) }
        ) {
            Text(text = stringResource(id = rightButtonTextResId))
        }
    }
}
