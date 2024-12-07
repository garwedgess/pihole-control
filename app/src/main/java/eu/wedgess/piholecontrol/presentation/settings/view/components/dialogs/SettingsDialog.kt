package eu.wedgess.piholecontrol.presentation.settings.view.components.dialogs

import androidx.compose.runtime.Composable
import eu.wedgess.piholecontrol.presentation.settings.SettingsContract
import eu.wedgess.piholecontrol.presentation.settings.model.SettingsDialogType

@Composable
fun SettingsDialog(dialogType: SettingsDialogType, onEvent: (SettingsContract.Event) -> Unit) {
    when (dialogType) {
        SettingsDialogType.None -> Unit
        is SettingsDialogType.RefreshInterval -> RefreshIntervalDialog(
            currentRefreshTime = dialogType.currentRefreshTime,
            onRefreshIntervalConfirmed = {
                onEvent(SettingsContract.Event.OnRefreshIntervalChanged(it))
            },
            onDismiss = { onEvent(SettingsContract.Event.OnDismissDialog) }
        )
    }
}