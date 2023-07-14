package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.ui.common.RadioGroup
import eu.wedgess.mihole.ui.logs.LogsContract
import eu.wedgess.mihole.ui.theme.MiHoleTheme

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
    MiHoleTheme {
        LogsLimitRadioButtonGroup(
            itemsList = LogsContract.UiState.availableLogLimits,
            selectedItem = LogsContract.UiState.availableLogLimits.last(),
            onLogLimitSelected = {}
        )
    }
}