package eu.wedgess.mihole.ui.app.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.view.components.dialog.DisableStatusDialog
import eu.wedgess.mihole.ui.app.view.components.dialog.EnableStatusDialog
import eu.wedgess.mihole.ui.app.viewmodel.AppViewModel
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.MainAppBar
import eu.wedgess.mihole.ui.navigation.MainNavigationGraph
import eu.wedgess.mihole.ui.navigation.bottom.BottomNavigationBar
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import timber.log.Timber
import kotlin.math.abs

@Composable
fun MiHoleApp(
    viewModel: AppViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    val backStackEntry = navHostController.currentBackStackEntryAsState()
    val localDensity = LocalDensity.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var appBarState by remember(uiState.currentConnection, uiState.status) {
        mutableStateOf(
            AppBarState(
                currentConnection = (uiState.currentConnection as? UiResult.Success)?.data,
                adBlockingEnabled = (uiState.status as? UiResult.Success)?.data == PiHoleStatus.ENABLED,
                connections = (uiState.connections as? UiResult.Success)?.data ?: emptyList(),
                bottomBarVisible = true
            )
        )
    }

    val bottomBarHeight = 80.dp
    val bottomBarHeightPx = with(localDensity) {
        bottomBarHeight.roundToPx().toFloat()
    }
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                Timber.i("POSTSCROLL ${available.y}")
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.value + delta
                bottomBarOffsetHeightPx.value =
                    newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val bottomPadding = remember {
        derivedStateOf {
            with(localDensity) { abs(bottomBarOffsetHeightPx.value.plus(bottomBarHeightPx)).toDp() }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(AppContract.Event.FetchSettings)
        viewModel.onEvent(AppContract.Event.FetchCurrentConnection)
        viewModel.onEvent(AppContract.Event.FetchConnections)
        viewModel.onEvent(AppContract.Event.FetchStatus)
    }

//    DisposableEffect(Unit) {
//        val refreshJob = viewModel.autoRefreshData()
//
//        onDispose {
//            refreshJob.cancel()
//        }
//    }

    val isDarkTheme = when (uiState.currentTheme) {
        UserPreferences.Theme.DARK -> true
        UserPreferences.Theme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    MiHoleTheme(
        darkTheme = isDarkTheme,
        dynamicColor = uiState.useDynamicThemeColors
    ) {
//        MaterialTheme.colorScheme.run {
//            SideEffect {
//                systemUiController.apply {
//                    setStatusBarColor(this@run.background)
//                    setNavigationBarColor(
//                        this@run.surfaceColorAtElevation(
//                            NavigationBarDefaults.Elevation
//                        )
//                    )
//                }
//            }
//        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(nestedScrollConnection),
                topBar = {
                    MainAppBar(
                        appBarState = appBarState,
                        onNavigateBack = { navHostController.navigateUp() },
                        onStatusClicked = {
                            if (appBarState.adBlockingEnabled) {
                                viewModel.onEvent(AppContract.Event.ShowDisabledStatusDialog)
                            } else {
                                viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)
                            }
                        },
                        onConnectionSelected = {
                            viewModel.onEvent(
                                AppContract.Event.OnConnectionSelected(
                                    it
                                )
                            )
                        }
                    )

                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = appBarState.bottomBarVisible,
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = slideOutHorizontally(targetOffsetX = { -it }),
                    ) {
                        BottomAppBar(
                            modifier = Modifier
                                .height(bottomBarHeight)
                                .graphicsLayer {
                                    translationY = -bottomBarOffsetHeightPx.value
                                }
                        ) {
                            BottomNavigationBar(
                                selectedItemRoute = backStackEntry.value?.destination?.route,
                                onNavigateTo = { route ->
                                    if (route != backStackEntry.value?.destination?.route) {
                                        navHostController.navigate(route)
                                    }
                                }
                            )
                        }
                    }
                },
                content = { contentPadding ->
                    AnimatedVisibility(visible = uiState.showDisableStatusDialog) {
                        DisableStatusDialog(
                            onDisableStatus = {
                                viewModel.onEvent(
                                    AppContract.Event.SetDisabledStatus(
                                        it
                                    )
                                )
                            },
                            onDismissDialog = { viewModel.onEvent(AppContract.Event.DismissDisabledStatusDialog) }
                        )
                    }
                    AnimatedVisibility(visible = uiState.showEnableStatusDialog) {
                        EnableStatusDialog(
                            onEnabledStatus = { viewModel.onEvent(AppContract.Event.SetEnabledStatus) },
                            onDismissDialog = { viewModel.onEvent(AppContract.Event.DismissEnabledStatusDialog) }
                        )
                    }
                    MainNavigationGraph(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                                bottom = if (appBarState.bottomBarVisible) {
                                    bottomPadding.value
                                } else {
                                    contentPadding.calculateBottomPadding()
                                },
                                top = contentPadding.calculateTopPadding(),
                                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
                            )
                        ),
                        navController = navHostController,
                        onComposing = { updateState ->
                            appBarState =
                                updateState.copy(
                                    currentConnection = (uiState.currentConnection as? UiResult.Success)?.data,
                                    adBlockingEnabled = (uiState.status as? UiResult.Success)?.data == PiHoleStatus.ENABLED,
                                    connections = (uiState.connections as? UiResult.Success)?.data,
                                    bottomBarVisible = updateState.bottomBarVisible
                                )
                        }
                    )
                }
            )
        }
    }
}