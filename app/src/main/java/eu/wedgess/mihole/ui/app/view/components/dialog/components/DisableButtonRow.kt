package eu.wedgess.mihole.ui.app.view.components.dialog.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun DisableButtonRow(
    selectedTime: Long,
    leftButtonTextResId: Int,
    leftValue: Long,
    rightButtonTextResId: Int,
    rightValue: Long,
    onTimeSelected: (Long) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
    ) {
        OutlinedButton(
            modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
            border = if (selectedTime == leftValue) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                ButtonDefaults.outlinedButtonBorder
            },
            onClick = { onTimeSelected(leftValue) }
        ) {
            Text(text = stringResource(id = leftButtonTextResId))
        }
        OutlinedButton(
            modifier = Modifier.weight(MiHoleTheme.dimens.weight.full),
            border = if (selectedTime == rightValue) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                ButtonDefaults.outlinedButtonBorder
            },
            onClick = { onTimeSelected(rightValue) }) {
            Text(text = stringResource(id = rightButtonTextResId))
        }
    }
}