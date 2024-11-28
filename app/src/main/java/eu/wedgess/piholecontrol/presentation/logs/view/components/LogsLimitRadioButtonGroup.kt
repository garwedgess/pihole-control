package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.presentation.common.RadioGroup
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

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