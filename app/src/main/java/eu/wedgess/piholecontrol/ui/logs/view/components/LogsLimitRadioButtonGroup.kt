package eu.wedgess.piholecontrol.ui.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.ui.common.RadioGroup
import eu.wedgess.piholecontrol.ui.logs.LogsContract
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme

@Composable
fun LogsLimitRadioButtonGroup(
    itemsList: List<Int>,
    selectedItem: Int,
    onLogLimitSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        modifier = modifier,
        itemsList = itemsList.toList(),
        selectedItem = selectedItem,
        labelFormatter = { it.toString() },
        onItemSelected = onLogLimitSelected
    )
}

@Preview
@Composable
private fun LogsLimitRadioButtonGroupPreview() {
    PiHoleControlTheme {
        LogsLimitRadioButtonGroup(
            itemsList = LogsContract.UiState.availableLogLimits,
            selectedItem = LogsContract.UiState.availableLogLimits.last(),
            onLogLimitSelected = {}
        )
    }
}