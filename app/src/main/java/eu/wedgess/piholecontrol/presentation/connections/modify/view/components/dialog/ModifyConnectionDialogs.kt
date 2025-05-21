package eu.wedgess.piholecontrol.presentation.connections.modify.view.components.dialog

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.common.components.AlertMessageDialog
import eu.wedgess.piholecontrol.presentation.common.components.LoadingDialog
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.model.ModifyConnectionDialogType

@OptIn(ExperimentalGetImage::class)
@Composable
fun ModifyConnectionDialogs(
    dialogType: ModifyConnectionDialogType,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    when (dialogType) {
        ModifyConnectionDialogType.None -> Unit

        is ModifyConnectionDialogType.FailedToSaveConnection -> AlertMessageDialog(
            titleText = stringResource(R.string.connection_dialog_title_saving_failed),
            messageText = dialogType.message.asString(),
            dismissText = stringResource(R.string.all_btn_ok),
            onDismiss = { onEvent(ModifyConnectionsContract.Event.OnDismissDialog) }
        )

        is ModifyConnectionDialogType.LoadingDialog -> LoadingDialog(
            message = dialogType.message.asString(),
            onDismiss = { onEvent(ModifyConnectionsContract.Event.OnDismissDialog) }
        )
    }
}
