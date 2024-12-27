package eu.wedgess.piholecontrol.presentation.app.view.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.presentation.app.model.NetworkStatusUiState
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun NetworkStatusRow(status: NetworkStatusUiState, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        modifier = modifier,
        visible = status.isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (status.isConnected) {
                        MaterialTheme.colorScheme.totalQueriesBackground
                    } else {
                        MaterialTheme.colorScheme.domainsOnAdListBackground
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                    vertical = PiHoleControlTheme.dimens.padding.itemContentSmall
                ),
                text = if (status.isConnected) {
                    "Connected to network"
                } else {
                    "Disconnected from network"
                },
                color = Color.White
            )

            Icon(
                modifier = Modifier.padding(
                    horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                    vertical = PiHoleControlTheme.dimens.padding.itemContentSmall
                ),
                imageVector = if (status.isConnected) {
                    Icons.Default.Cloud
                } else {
                    Icons.Default.CloudOff
                },
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@ThemePreview
@Composable
private fun NetworkStatusRowPreview(
    @PreviewParameter(NetworkStatusRowPreviewProvider::class) state: NetworkStatusUiState
) {
    PiHoleControlTheme {
        Surface {
            NetworkStatusRow(state)
        }
    }
}

private class NetworkStatusRowPreviewProvider : PreviewParameterProvider<NetworkStatusUiState> {
    override val values: Sequence<NetworkStatusUiState>
        get() = sequenceOf(
            NetworkStatusUiState(
                isVisible = true,
                networkConnectionState = NetworkConnectionState.Available
            ),
            NetworkStatusUiState(
                isVisible = true,
                networkConnectionState = NetworkConnectionState.Unavailable
            )
        )

}
