package eu.wedgess.mihole.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import eu.wedgess.mihole.ui.app.view.components.CurrentConnectionStatus
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(appBarState: AppBarState, onNavigateBack: () -> Unit) {
    val titleAlpha: Float by animateFloatAsState(
        targetValue = if (!appBarState.showSearchView) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (!appBarState.showSearchView) 300 else 20,
            easing = LinearEasing,
        )
    )
    TopAppBar(
        title = {
            appBarState.currentConnection?.takeIf { appBarState.displayConnection }?.run {
                CurrentConnectionStatus(
                    modifier = Modifier.graphicsLayer { alpha = titleAlpha },
                    currentConnection = this
                )
            } ?: Text(
                modifier = Modifier.graphicsLayer { alpha = titleAlpha },
                text = appBarState.title.asString()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            AnimatedVisibility(visible = appBarState.showSearchView) {
                appBarState.searchContent?.invoke()
            }
            AnimatedVisibility(visible = !appBarState.showSearchView) {
                appBarState.actions?.invoke(this@TopAppBar)
            }
        },
        navigationIcon = {
            if (appBarState.showNavigateBackIcon) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}