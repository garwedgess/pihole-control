package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    onConnectionClick: ((ConnectionEntity) -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null
) {
    val isSearch = appBarState is AppBarState.Search
    val normalAlpha by animateFloatAsState(
        targetValue = if (isSearch) 0f else 1f,
        animationSpec = tween(durationMillis = if (isSearch) 100 else 200),
        label = "normal app bar content alpha"
    )
    val searchAlpha by animateFloatAsState(
        targetValue = if (isSearch) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (isSearch) 200 else 100,
            delayMillis = if (isSearch) 100 else 0
        ),
        label = "search app bar content alpha"
    )
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0.dp),
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (!isSearch || normalAlpha > MIN_VISIBLE_ALPHA) {
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

                if (isSearch || searchAlpha > MIN_VISIBLE_ALPHA) {
                    Box(
                        modifier = Modifier
                            .alpha(searchAlpha)
                            .graphicsLayer {
                                scaleX = SEARCH_COLLAPSED_SCALE +
                                    (1f - SEARCH_COLLAPSED_SCALE) * searchAlpha
                                transformOrigin = TransformOrigin(0f, 0.5f)
                            }
                    ) {
                        (appBarState as? AppBarState.Search)?.searchContent?.invoke()
                    }
                }
            }
        },
        actions = {
            if (normalAlpha > MIN_VISIBLE_ALPHA) {
                AnimatedVisibility(visible = !isSearch) {
                    Row(
                        modifier = Modifier.alpha(normalAlpha),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when (appBarState) {
                            is AppBarState.Contextual -> appBarState.actions(this)
                            is AppBarState.Normal -> appBarState.actions?.invoke(this)
                            is AppBarState.Search -> Unit
                        }
                        if (
                            appBarState !is AppBarState.Contextual &&
                            (
                                appBarState.showSettingsAction ||
                                    (appBarState as? AppBarState.Normal)?.overflowActions != null
                                )
                        ) {
                            AppBarOverflowAction(
                                overflowActions = (appBarState as? AppBarState.Normal)
                                    ?.overflowActions,
                                showSettingsAction = appBarState.showSettingsAction,
                                onSettingsClick = onSettingsClick
                            )
                        }
                    }
                }
            }
        },
        navigationIcon = {
            if (appBarState is AppBarState.Contextual && normalAlpha > MIN_VISIBLE_ALPHA) {
                Box(modifier = Modifier.alpha(normalAlpha)) {
                    IconButton(onClick = appBarState.onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.all_btn_close),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            } else if (appBarState.showNavigateBackIcon && normalAlpha > MIN_VISIBLE_ALPHA) {
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

@Composable
private fun AppBarOverflowAction(
    overflowActions: (@Composable ColumnScope.(dismiss: () -> Unit) -> Unit)?,
    showSettingsAction: Boolean,
    onSettingsClick: (() -> Unit)?
) {
    var showOverflowMenu by remember { mutableStateOf(false) }
    val dismiss = { showOverflowMenu = false }

    Box {
        IconButton(onClick = { showOverflowMenu = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = stringResource(R.string.all_cd_more_options),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            expanded = showOverflowMenu,
            onDismissRequest = { showOverflowMenu = false }
        ) {
            overflowActions?.invoke(this, dismiss)
            if (overflowActions != null && showSettingsAction) {
                HorizontalDivider()
            }
            if (showSettingsAction) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.nav_title_settings)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        showOverflowMenu = false
                        onSettingsClick?.invoke()
                    }
                )
            }
        }
    }
}

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
            onConnectionClick = {},
            onSettingsClick = {}
        )
    }
}

private class MainAppBarPreviewParameterProvider : PreviewParameterProvider<AppBarState> {
    override val values = sequenceOf(
        AppBarState.Normal(
            title = UiText.StringResource(R.string.appbar_title_connections),
            currentConnection = null,
            showNavigateBackIcon = true,
            connections = emptyList(),
            actions = {}
        ),
        AppBarState.Normal(
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
            }
        ),
        AppBarState.Normal(
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
            }
        ),
        AppBarState.Search(
            title = UiText.StringResource(R.string.nav_title_logs),
            currentConnection = ConnectionEntity.default,
            showNavigateBackIcon = false,
            connections = emptyList(),
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
            }
        )
    )
}
