package eu.wedgess.piholecontrol.presentation.connections.modify.view

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.view.components.ModifyConnectionContent
import eu.wedgess.piholecontrol.presentation.connections.modify.view.components.dialog.ModifyConnectionDialogs
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@ExperimentalGetImage
@Composable
fun ModifyConnectionScreen(
    uiState: ModifyConnectionsContract.UiState,
    onEvent: (ModifyConnectionsContract.Event) -> Unit
) {
    Scaffold(
        modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.screenContent)
    ) { paddingValues ->
        ModifyConnectionContent(
            paddingValues = paddingValues,
            uiState = uiState,
            onEvent = onEvent
        )

        ModifyConnectionDialogs(uiState.dialogType, onEvent)
    }
}