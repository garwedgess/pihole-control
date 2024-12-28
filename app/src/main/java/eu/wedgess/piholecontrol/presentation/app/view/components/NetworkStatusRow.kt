package eu.wedgess.piholecontrol.presentation.app.view.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.presentation.app.model.NetworkStatusUiState
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun NetworkStatusRow(status: NetworkStatusUiState, modifier: Modifier = Modifier) {
    val targetColor = if (status.isConnected) {
        MaterialTheme.colorScheme.totalQueriesBackground
    } else {
        MaterialTheme.colorScheme.domainsOnAdListBackground
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "background colour"
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedColor)
            .animateContentSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedContent(
            targetState = status.isConnected,
            label = "text animation",
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(animationSpec = tween(500)),
                    initialContentExit = fadeOut(animationSpec = tween(500))
                )
            }
        ) { isConnected ->
            Text(
                modifier = Modifier.padding(
                    horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                    vertical = PiHoleControlTheme.dimens.padding.itemContentSmall
                ),
                text = if (isConnected) {
                    stringResource(R.string.network_connected)
                } else {
                    stringResource(R.string.network_disconnected)
                },
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        AnimatedContent(
            targetState = status.isConnected,
            label = "icon animation",
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(animationSpec = tween(500)),
                    initialContentExit = fadeOut(animationSpec = tween(500))
                )
            }
        ) { isConnected ->
            Icon(
                modifier = Modifier
                    .padding(
                        horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                        vertical = PiHoleControlTheme.dimens.padding.itemContentSmall
                    )
                    .size(24.dp),
                imageVector = if (isConnected) {
                    Icons.Default.Cloud
                } else {
                    Icons.Default.CloudOff
                },
                contentDescription = if (isConnected) {
                    stringResource(R.string.network_connected)
                } else {
                    stringResource(R.string.network_disconnected)
                },
                tint = MaterialTheme.colorScheme.onErrorContainer
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
