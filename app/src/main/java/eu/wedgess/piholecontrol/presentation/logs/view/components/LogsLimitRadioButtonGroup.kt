package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.RadioGroup
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.LogsContract
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogsLimitRadioButtonGroup(
    itemsList: List<Int>,
    selectedItem: Int,
    onLogLimitClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        modifier = modifier,
        itemsList = itemsList.toList(),
        selectedItem = selectedItem,
        labelFormatter = { it.toString() },
        onItemClick = onLogLimitClick
    )
}

@ThemePreview
@Composable
private fun LogsLimitRadioButtonGroupPreview() {
    PiHoleControlTheme {
        LogsLimitRadioButtonGroup(
            itemsList = LogsContract.BottomSheetUiState.availableLogLimits,
            selectedItem = LogsContract.BottomSheetUiState.availableLogLimits.last(),
            onLogLimitClick = {}
        )
    }
}
