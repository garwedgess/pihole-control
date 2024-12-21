package eu.wedgess.piholecontrol.presentation.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.connections.list.view.components.ConnectionListItem
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.35f }
    )

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
            dismissState.reset()
        }
    }

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val backgroundColor by
            animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> {
                        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                        } else {
                            MaterialTheme.colorScheme.background
                        }
                    }

                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.background
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                },
                label = "delete background color"
            )
            val iconColor by
            animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.Settled -> {
                        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.4f)
                        }
                    }

                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.onBackground
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.onErrorContainer
                },
                label = "delete background color"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(PiHoleControlTheme.dimens.padding.screenContent),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }
    ) {
        content()
    }
}

@ThemePreview
@Composable
private fun SwipeToDeleteItemPreview() {
    PiHoleControlTheme {
        Surface {
            SwipeToDeleteItem(
                content = {
                    ConnectionListItem(
                        connectionInfo = ConnectionEntity.default,
                        onDeleteClick = {},
                        onEditClick = {},
                        onSetActiveClick = {}
                    )
                },
                onDelete = {}
            )
        }
    }
}
