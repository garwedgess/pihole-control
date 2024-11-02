package eu.wedgess.mihole.ui.app.view.components.dialog

import androidx.compose.runtime.Composable
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.model.AppDialogType

@Composable
fun AppDialogs(dialogType: AppDialogType, onEvent: (AppContract.Event) -> Unit) {
    when (dialogType) {
        AppDialogType.None -> Unit
        AppDialogType.EnableAdBlocking -> EnableStatusDialog(
            onEnabledStatus = { onEvent(AppContract.Event.SetEnabledStatus) },
            onDismissDialog = { onEvent(AppContract.Event.DismissDialog) }
        )

        AppDialogType.DisableAdBlocking -> DisableStatusDialog(
            onDisableStatus = { onEvent(AppContract.Event.SetDisabledStatus(it)) },
            onDismissDialog = { onEvent(AppContract.Event.DismissDialog) }
        )
    }
}