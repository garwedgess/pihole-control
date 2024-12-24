package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.components.RadioGroup
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryStatus
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LogsEntryRadioButtonGroup(
    itemsList: Array<LogEntryStatus>,
    selectedItem: LogEntryStatus,
    onLogEntryStatusClick: (LogEntryStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        modifier = modifier,
        itemsList = itemsList.toList(),
        selectedItem = selectedItem,
        labelFormatter = { it.uiText.asString() },
        onItemClick = onLogEntryStatusClick
    )
}

@ThemePreview
@Composable
private fun LogsEntryRadioButtonGroupPreview() {
    PiHoleControlTheme {
        LogsEntryRadioButtonGroup(
            itemsList = LogEntryStatus.entries.toTypedArray(),
            selectedItem = LogEntryStatus.ALL,
            onLogEntryStatusClick = {}
        )
    }
}
