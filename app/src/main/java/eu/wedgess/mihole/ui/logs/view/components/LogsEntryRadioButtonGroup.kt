package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.ui.common.RadioGroup
import eu.wedgess.mihole.ui.logs.model.LogEntryStatus
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun LogsEntryRadioButtonGroup(
    itemsList: Array<LogEntryStatus>,
    selectedItem: LogEntryStatus,
    onLogEntryStatusSelected: (LogEntryStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        modifier = modifier,
        itemsList = itemsList.toList(),
        selectedItem = selectedItem,
        labelFormatter = { it.uiText.asString() },
        onItemSelected = onLogEntryStatusSelected
    )
}

@Preview
@Composable
private fun LogsEntryRadioButtonGroupPreview() {
    MiHoleTheme {
        LogsEntryRadioButtonGroup(
            itemsList = LogEntryStatus.values(),
            selectedItem = LogEntryStatus.ALL,
            onLogEntryStatusSelected = {})
    }
}