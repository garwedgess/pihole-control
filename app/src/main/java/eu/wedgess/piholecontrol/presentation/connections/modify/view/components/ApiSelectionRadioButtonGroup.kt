package eu.wedgess.piholecontrol.presentation.connections.modify.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.common.components.RadioGroup
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ApiSelectionRadioButtonGroup(
    itemsList: Array<PiHoleApiVersion>,
    selectedItem: PiHoleApiVersion,
    onApiVersionChange: (PiHoleApiVersion) -> Unit,
    modifier: Modifier = Modifier
) {
    RadioGroup(
        modifier = modifier,
        itemsList = itemsList.toList(),
        selectedItem = selectedItem,
        labelFormatter = { it.label.asString() },
        onItemClick = onApiVersionChange
    )
}

@ThemePreview
@Composable
private fun ApiSelectionRadioButtonGroupPreview() {
    PiHoleControlTheme {
        ApiSelectionRadioButtonGroup(
            itemsList = PiHoleApiVersion.entries.toTypedArray(),
            selectedItem = PiHoleApiVersion.Version5,
            onApiVersionChange = {}
        )
    }
}