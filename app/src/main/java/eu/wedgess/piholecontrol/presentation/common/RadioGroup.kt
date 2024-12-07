package eu.wedgess.piholecontrol.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun <T> RadioGroup(
    itemsList: List<T>,
    selectedItem: T,
    labelFormatter: @Composable (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
) {
    Row(modifier = modifier.wrapContentWidth(), horizontalArrangement = horizontalArrangement) {

        itemsList.forEachIndexed { index, item ->

            OutlinedButton(
                onClick = { onItemSelected(item) },
                modifier = when (index) {
                    0 ->
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(if (selectedItem == item) 1f else 0f)

                    else ->
                        Modifier
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (selectedItem == item) 1f else 0f)
                },
                shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = PiHoleControlTheme.dimens.size.cornerRadius,
                        topEnd = 0.dp,
                        bottomStart = PiHoleControlTheme.dimens.size.cornerRadius,
                        bottomEnd = 0.dp
                    )

                    itemsList.size - 1 -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = PiHoleControlTheme.dimens.size.cornerRadius,
                        bottomStart = 0.dp,
                        bottomEnd = PiHoleControlTheme.dimens.size.cornerRadius
                    )

                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                },
                border = BorderStroke(
                    1.dp, if (selectedItem == item) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = PiHoleControlTheme.dimens.weight.radioGroupUnSelectedBorderAlpha)
                    }
                ),
                colors = if (selectedItem == item) {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = PiHoleControlTheme.dimens.weight.minAlpha),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                AnimatedVisibility(visible = selectedItem == item) {
                    Icon(
                        modifier = Modifier
                            .padding(end = PiHoleControlTheme.dimens.padding.itemContent)
                            .size(PiHoleControlTheme.dimens.size.radioGroupSelectedIcon),
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(text = labelFormatter(item))
            }
        }
    }
}

@Preview
@Composable
private fun RadioGroupPreview() {
    PiHoleControlTheme {
        RadioGroup(
            itemsList = LogEntryStatus.entries,
            selectedItem = LogEntryStatus.ALL,
            labelFormatter = { it.uiText.asString() },
            onItemSelected = {}
        )
    }
}