package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.connections.list.view.components.ConnectionListItem
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    enableDismissFromStartToEnd: Boolean = false,
    enableDismissFromEndToStart: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val containerWidth = remember { mutableFloatStateOf(0f) }

    val dragState: AnchoredDraggableState<SwipeToRevealValue> = remember {
        AnchoredDraggableState(
            initialValue = SwipeToRevealValue.Settled,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { with(density) { 125.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = decayAnimationSpec
        )
    }

    LaunchedEffect(dragState.currentValue) {
        if (dragState.currentValue == SwipeToRevealValue.EndToStart) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(dragState.offset) {
        if (dragState.currentValue == SwipeToRevealValue.EndToStart) {
            if (dragState.offset == -containerWidth.floatValue) {
                delay(200)
                onDelete()
                delay(200)
                dragState.snapTo(SwipeToRevealValue.Settled)
            }
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = lerp(
            MaterialTheme.colorScheme.inverseSurface, // White
            MaterialTheme.colorScheme.error,
            dragState.progress(
                from = SwipeToRevealValue.Settled,
                to = SwipeToRevealValue.EndToStart
            ).div(0.1f).coerceIn(0f, 1f)
        ),
        animationSpec = tween(durationMillis = 400),
        label = "background color"
    )

    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Box(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .onGloballyPositioned {
                val width = it.size.width.toFloat()
                containerWidth.floatValue = width
                dragState.updateAnchors(
                    newAnchors = DraggableAnchors {
                        SwipeToRevealValue.Settled at 0f
                        if (enableDismissFromStartToEnd) {
                            SwipeToRevealValue.StartToEnd at if (isRtl) -width else width
                        }
                        if (enableDismissFromEndToStart) {
                            SwipeToRevealValue.EndToStart at if (isRtl) width else -width
                        }
                    }
                )
            }
            .anchoredDraggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                enabled = dragState.currentValue == SwipeToRevealValue.Settled
            ),
        propagateMinConstraints = true
    ) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .background(backgroundColor)
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        Row(
            content = content,
            modifier = Modifier
                .anchoredDraggable(
                    state = dragState,
                    orientation = Orientation.Horizontal
                )
                .offset {
                    IntOffset(
                        x = dragState.offset
                            .takeIf { !it.isNaN() }
                            ?.run { this.roundToInt() } ?: 0,
                        y = 0
                    )
                }
        )
    }
}

private enum class SwipeToRevealValue { Settled, StartToEnd, EndToStart }

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
