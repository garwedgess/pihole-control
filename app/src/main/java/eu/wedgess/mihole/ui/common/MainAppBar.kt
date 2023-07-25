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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.app.view.components.CurrentConnectionStatus
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.common.search.SearchContent
import eu.wedgess.mihole.ui.common.search.SearchState
import eu.wedgess.mihole.ui.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    appBarState: AppBarState,
    onNavigateBack: () -> Unit,
    onStatusClicked: () -> Unit,
    onConnectionSelected: (MiHolesInfo) -> Unit
) {
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
                appBarState.actions?.invoke(this@TopAppBar)
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
    MiHoleTheme {
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
            currentConnection = MiHolesInfo.default,
            adBlockingEnabled = true,
            showNavigateBackIcon = false,
            connections = listOf(MiHolesInfo.default),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_filters),
            currentConnection = MiHolesInfo.default,
            adBlockingEnabled = false,
            showNavigateBackIcon = false,
            connections = listOf(MiHolesInfo.default),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_logs),
            currentConnection = MiHolesInfo.default,
            showNavigateBackIcon = false,
            connections = emptyList(),
            actions = { FilterTopBarActions(onSearchClicked = {}) },
            searchContent = {
                SearchContent(
                    state = SearchState<List<MiHolesInfo>>(),
                    onQueryChanged = { },
                    onClosed = { }
                )
            },
            showSearchView = true
        )
    )
}