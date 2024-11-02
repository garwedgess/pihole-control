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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.view.components.dialog.AppDialogs
import eu.wedgess.mihole.ui.app.viewmodel.AppViewModel
import eu.wedgess.mihole.ui.app.model.AppBarState
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
    val currentLayoutDirection = LocalLayoutDirection.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomBarHeight = 80.dp
    val bottomBarHeightPx = with(localDensity) {
        bottomBarHeight.roundToPx().toFloat()
    }
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                bottomBarOffsetHeightPx.floatValue =
                    newOffset.coerceIn(-bottomBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val bottomPadding = remember {
        derivedStateOf {
            with(localDensity) {
                abs(bottomBarOffsetHeightPx.floatValue.plus(bottomBarHeightPx)).toDp()
            }
        }
    }

    val isDarkTheme = when (uiState.appInfo.currentTheme) {
        UserPreferences.Theme.DARK -> true
        UserPreferences.Theme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    MiHoleTheme(
        darkTheme = isDarkTheme,
        dynamicColor = uiState.appInfo.useDynamicThemeColors
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(nestedScrollConnection),
                topBar = {
                    MainAppBar(
                        appBarState = uiState.appBarState,
                        onNavigateBack = { navHostController.navigateUp() },
                        onStatusClicked = {
                            if (uiState.appBarState.adBlockingEnabled) {
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
                        visible = uiState.appBarState.bottomBarVisible,
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = slideOutHorizontally(targetOffsetX = { -it }),
                    ) {
                        BottomAppBar(
                            modifier = Modifier
                                .height(bottomBarHeight)
                                .graphicsLayer {
                                    translationY = -bottomBarOffsetHeightPx.floatValue
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
                    MainNavigationGraph(
                        modifier = Modifier.padding(
                            PaddingValues(
                                start = contentPadding.calculateStartPadding(currentLayoutDirection),
                                bottom = if (uiState.appBarState.bottomBarVisible) {
                                    bottomPadding.value
                                } else {
                                    contentPadding.calculateBottomPadding()
                                },
                                top = contentPadding.calculateTopPadding(),
                                end = contentPadding.calculateEndPadding(currentLayoutDirection)
                            )
                        ),
                        navController = navHostController,
                        onComposing = { updateState ->
                            Timber.d("UpdatedState: $updateState")
                            viewModel.onEvent(AppContract.Event.UpdateAppBarState(updateState))
                        }
                    )
                    AppDialogs(uiState.dialogType, onEvent = viewModel::onEvent)
                }
            )
        }
    }
}