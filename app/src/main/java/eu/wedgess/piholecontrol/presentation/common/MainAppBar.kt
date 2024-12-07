package eu.wedgess.piholecontrol.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    appBarState: AppBarState,
    onNavigateBack: () -> Unit,
    onStatusClicked: () -> Unit,
    onConnectionSelected: (ConnectionEntity) -> Unit
) {
    val titleAlpha: Float by animateFloatAsState(
        targetValue = if (appBarState.showSearchView) 0f else 01f,
        animationSpec = tween(
            durationMillis = if (appBarState.showSearchView) 20 else 300,
            easing = LinearEasing,
        ),
        label = "title animation"
    )
    CenterAlignedTopAppBar(
        title = {
            appBarState.currentConnection?.takeIf { appBarState.displayConnection }?.run {
                CurrentConnectionStatus(
                    modifier = Modifier.graphicsLayer { alpha = titleAlpha },
                    currentConnection = this,
                    connections = appBarState.connections ?: emptyList(),
                    adBlockingEnabled = appBarState.adBlockingEnabled,
                    onStatusClicked = { onStatusClicked() },
                    onConnectionSelected = { onConnectionSelected(it) }
                )
            } ?: Text(
                modifier = Modifier.graphicsLayer { alpha = titleAlpha },
                text = appBarState.title.asString()
            )
        },
        actions = {
            AnimatedVisibility(visible = appBarState.showSearchView) {
                appBarState.searchContent?.invoke()
            }
            AnimatedVisibility(visible = !appBarState.showSearchView) {
                appBarState.actions?.invoke(this@CenterAlignedTopAppBar)
            }
        },
        navigationIcon = {
            if (appBarState.showNavigateBackIcon) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}

@ThemePreview
@Composable
fun MainAppBarPreview(
    @PreviewParameter(MainAppBarPreviewParameterProvider::class) appbarState: AppBarState
) {
    PiHoleControlTheme {
        MainAppBar(appBarState = appbarState, onNavigateBack = {}, onStatusClicked = {}, onConnectionSelected = {})
    }
}

private class MainAppBarPreviewParameterProvider : PreviewParameterProvider<AppBarState> {
    override val values = sequenceOf(
        AppBarState(
            title = UiText.StringResource(R.string.appbar_title_connections),
            currentConnection = null,
            showNavigateBackIcon = true,
            connections = emptyList(),
            actions = {},
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_filters),
            currentConnection = ConnectionEntity.default,
            adBlockingEnabled = true,
            showNavigateBackIcon = false,
            connections = listOf(ConnectionEntity.default),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_filters),
            currentConnection = ConnectionEntity.default,
            adBlockingEnabled = false,
            showNavigateBackIcon = false,
            connections = listOf(ConnectionEntity.default),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_logs),
            currentConnection = ConnectionEntity.default,
            showNavigateBackIcon = false,
            connections = emptyList(),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = {
                SearchContent(
                    placeHolderText = "Search for filter...",
                    searchQuery = "",
                    showSearchView = true,
                    onSearch = {},
                    onClearSearchQuery = {},
                    onQueryChange = {},
                    onExpandedChange = {}
                )
            },
            showSearchView = true
        )
    )
}