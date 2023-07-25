package eu.wedgess.mihole.ui.app.view.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.window.Dialog
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.app.view.components.dialog.components.DisableStatusDialogContent
import eu.wedgess.mihole.ui.app.view.components.dialog.components.EnableStatusDialogContent
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun StatusDialog(
    currentStatus: PiHoleStatus,
    onDisableStatus: (duration: Long) -> Unit,
    onEnabledStatus: () -> Unit,
    onDismissDialog: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissDialog() }) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = MiHoleTheme.dimens.size.dialogTonalElevation,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            if (currentStatus == PiHoleStatus.ENABLED) {
                DisableStatusDialogContent(
                    onDisableStatus = { onDisableStatus(it) },
                    onDismiss = { onDismissDialog() }
                )
            } else {
                EnableStatusDialogContent(
                    onEnable = { onEnabledStatus() },
                    onDismiss = { onDismissDialog() }
                )
            }
        }
    }
}

@ThemePreview
@Composable
private fun StatusDialogPreview(
    @PreviewParameter(StatusDialogPreviewParameters::class) status: PiHoleStatus
) {
    MiHoleTheme {
        StatusDialog(
            currentStatus = status,
            onDisableStatus = {},
            onEnabledStatus = { },
            onDismissDialog = {}
        )
    }
}

private class StatusDialogPreviewParameters : PreviewParameterProvider<PiHoleStatus> {
    override val values = sequenceOf(
        PiHoleStatus.ENABLED,
        PiHoleStatus.DISABLED,
        PiHoleStatus.UNKNOWN
    )
}