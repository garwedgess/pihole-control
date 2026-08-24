package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.filters.model.FilterByOption
import eu.wedgess.piholecontrol.presentation.filters.view.components.actions.FilterTopBarActions
import eu.wedgess.piholecontrol.presentation.navigation.tabs.FilterTab
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    appBarState: AppBarState,
    onNavigateBack: (() -> Unit)? = null,
    onStatusClick: (() -> Unit)? = null,
    onConnectionClick: ((ConnectionEntity) -> Unit)? = null
) {
    val normalAlpha by animateFloatAsState(
        targetValue = if (appBarState.showSearchView) 0f else 1f,
        animationSpec = tween(durationMillis = if (appBarState.showSearchView) 100 else 200),
        label = "normal app bar content alpha"
    )
    val searchAlpha by animateFloatAsState(
        targetValue = if (appBarState.showSearchView) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (appBarState.showSearchView) 200 else 100,
            delayMillis = if (appBarState.showSearchView) 100 else 0
        ),
        label = "search app bar content alpha"
    )
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (!appBarState.showSearchView || normalAlpha > MIN_VISIBLE_ALPHA) {
                    Box(modifier = Modifier.alpha(normalAlpha)) {
                        appBarState.currentConnection?.takeIf { appBarState.displayConnection }?.run {
                            CurrentConnectionStatus(
                                modifier = Modifier.graphicsLayer { alpha = normalAlpha },
                                currentConnection = this,
                                connections = appBarState.connections ?: emptyList(),
                                adBlockingEnabled = appBarState.adBlockingEnabled,
                                onStatusClick = { onStatusClick?.invoke() },
                                onConnectionClick = { onConnectionClick?.invoke(it) }
                            )
                        } ?: Text(
                            modifier = Modifier.graphicsLayer { alpha = normalAlpha },
                            text = appBarState.title.asString()
                        )
                    }
                }

                if (appBarState.showSearchView || searchAlpha > MIN_VISIBLE_ALPHA) {
                    Box(
                        modifier = Modifier
                            .alpha(searchAlpha)
                            .graphicsLayer {
                                scaleX = SEARCH_COLLAPSED_SCALE +
                                    (1f - SEARCH_COLLAPSED_SCALE) * searchAlpha
                                transformOrigin = TransformOrigin(0f, 0.5f)
                            }
                    ) {
                        appBarState.searchContent?.invoke()
                    }
                }
            }
        },
        actions = {
            if (normalAlpha > MIN_VISIBLE_ALPHA) {
                AnimatedVisibility(visible = !appBarState.showSearchView) {
                    Box(modifier = Modifier.alpha(normalAlpha)) {
                        appBarState.actions?.invoke(this@CenterAlignedTopAppBar)
                    }
                }
            }
        },
        navigationIcon = {
            if (appBarState.showNavigateBackIcon && normalAlpha > MIN_VISIBLE_ALPHA) {
                Box(modifier = Modifier.alpha(normalAlpha)) {
                    IconButton(onClick = { onNavigateBack?.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.all_cd_back),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    )
}

private const val MIN_VISIBLE_ALPHA = 0.01f
private const val SEARCH_COLLAPSED_SCALE = 0.92f

@ThemePreview
@Composable
private fun MainAppBarPreview(
    @PreviewParameter(MainAppBarPreviewParameterProvider::class) appbarState: AppBarState
) {
    PiHoleControlTheme {
        MainAppBar(
            appBarState = appbarState,
            onNavigateBack = {},
            onStatusClick = {},
            onConnectionClick = {}
        )
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
            actions = {
                FilterTopBarActions(
                    onSearchClick = {},
                    onFilterByClick = {},
                    onDismissFiltering = {},
                    onFilterByOptionSelected = {},
                    isFilterByMenuVisible = false,
                    availableFilterByOptions = emptyList(),
                    selectedFilterByOptions = emptyList()
                )
            },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_filters),
            currentConnection = ConnectionEntity.default,
            adBlockingEnabled = false,
            showNavigateBackIcon = false,
            connections = listOf(ConnectionEntity.default),
            actions = {
                FilterTopBarActions(
                    onSearchClick = {},
                    onFilterByClick = {},
                    onDismissFiltering = {},
                    onFilterByOptionSelected = {},
                    isFilterByMenuVisible = true,
                    availableFilterByOptions = FilterByOption.getByTab(FilterTab.AllowList),
                    selectedFilterByOptions = FilterByOption.getByTab(FilterTab.AllowList)
                )
            },
            searchContent = null,
            showSearchView = false
        ),
        AppBarState(
            title = UiText.StringResource(R.string.nav_title_logs),
            currentConnection = ConnectionEntity.default,
            showNavigateBackIcon = false,
            connections = emptyList(),
            actions = {
                FilterTopBarActions(
                    onSearchClick = {},
                    onFilterByClick = {},
                    onDismissFiltering = {},
                    onFilterByOptionSelected = {},
                    isFilterByMenuVisible = false,
                    availableFilterByOptions = emptyList(),
                    selectedFilterByOptions = emptyList()
                )
            },
            searchContent = {
                SearchContent(
                    placeHolderText = stringResource(R.string.filter_search_placeholder),
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
