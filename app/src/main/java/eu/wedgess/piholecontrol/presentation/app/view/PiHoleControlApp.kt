package eu.wedgess.piholecontrol.presentation.app.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.wedgess.piholecontrol.presentation.app.AppContract
import eu.wedgess.piholecontrol.presentation.app.model.NetworkStatusUiState
import eu.wedgess.piholecontrol.presentation.app.view.components.NetworkStatusRow
import eu.wedgess.piholecontrol.presentation.app.view.components.dialog.AppDialogs
import eu.wedgess.piholecontrol.presentation.app.viewmodel.AppViewModel
import eu.wedgess.piholecontrol.presentation.common.components.MainAppBar
import eu.wedgess.piholecontrol.presentation.navigation.bottom.BottomNavigationBar
import eu.wedgess.piholecontrol.presentation.navigation.graphs.MainNavigationGraph
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

@Composable
fun PiHoleControlApp(
    isDarkTheme: Boolean,
    useDynamicColors: Boolean,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    val navHostController = rememberNavController()
    val backStackEntry = navHostController.currentBackStackEntryAsState()
    val localDensity = LocalDensity.current
    val currentLayoutDirection = LocalLayoutDirection.current
    val localContext = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val dismissSnackbarState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value != SwipeToDismissBoxValue.Settled) {
            snackbarHostState.currentSnackbarData?.dismiss()
            true
        } else {
            false
        }
    })

    val bottomBarHeight = remember { mutableFloatStateOf(0f) }
    val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val bottomBarOffsetOriginal by animateFloatAsState(targetValue = 0f, label = "")
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = bottomBarOffsetHeightPx.floatValue + delta
                bottomBarOffsetHeightPx.floatValue =
                    newOffset.coerceIn(-bottomBarHeight.floatValue, 0f)
                Timber.d("Delta: $delta newOffset: $newOffset")
                return Offset.Zero
            }
        }
    }

    val bottomPadding = remember {
        derivedStateOf {
            with(localDensity) {
                abs(bottomBarOffsetHeightPx.floatValue.plus(bottomBarHeight.floatValue)).toDp()
            }
        }
    }

    PiHoleControlTheme(
        darkTheme = isDarkTheme,
        dynamicColor = useDynamicColors
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.nestedScroll(nestedScrollConnection),
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { data ->
                        SwipeToDismissBox(
                            state = dismissSnackbarState,
                            backgroundContent = {},
                            content = {
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                topBar = {
                    MainAppBar(
                        appBarState = uiState.appBarState,
                        onNavigateBack = { navHostController.navigateUp() },
                        onStatusClick = {
                            if (uiState.appBarState.adBlockingEnabled) {
                                viewModel.onEvent(AppContract.Event.ShowDisabledStatusDialog)
                            } else {
                                viewModel.onEvent(AppContract.Event.ShowEnabledStatusDialog)
                            }
                        },
                        onConnectionClick = {
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
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it }),
                    ) {
                        BottomAppBar(
                            modifier = Modifier
                                .onGloballyPositioned { coordinates ->
                                    bottomBarHeight.floatValue =
                                        coordinates.size.height.toFloat()
                                }
                                .graphicsLayer {
                                    translationY = -bottomBarOffsetHeightPx.floatValue
                                }
                        ) {
                            BottomNavigationBar(
                                selectedItemRoute = backStackEntry.value?.destination?.route,
                                onNavigateTo = { route ->
                                    if (route != backStackEntry.value?.destination) {
                                        navHostController.navigate(route)
                                    }
                                }
                            )
                        }
                    }
                },
                content = { contentPadding ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .animateContentSize()
                            .padding(
                                PaddingValues(
                                    start = contentPadding.calculateStartPadding(
                                        currentLayoutDirection
                                    ),
                                    bottom = if (uiState.appBarState.bottomBarVisible) {
                                        bottomPadding.value
                                    } else {
                                        contentPadding.calculateBottomPadding()
                                    },
                                    top = contentPadding.calculateTopPadding(),
                                    end = contentPadding.calculateEndPadding(currentLayoutDirection)
                                )
                            )
                    ) {
                        AnimatedContent(
                            targetState = uiState.networkConnectionState,
                            label = "Network Status Animation",
                            contentKey = { it.isVisible }
                        ) { targetState: NetworkStatusUiState ->
                            if (targetState.isVisible) {
                                NetworkStatusRow(status = targetState)
                            }
                        }
                        MainNavigationGraph(
                            navController = navHostController,
                            onComposing = { updateState ->
                                viewModel.onEvent(AppContract.Event.UpdateAppBarState(updateState))
                            },
                            onResetBottomAppBarOffset = {
                                bottomBarOffsetHeightPx.floatValue = bottomBarOffsetOriginal
                            },
                            showSnackbarMessage = { msg ->
                                localCoroutineScope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = msg.asString(context = localContext),
                                        duration = SnackbarDuration.Indefinite
                                    )
                                    if (result == SnackbarResult.Dismissed) {
                                        delay(500)
                                        dismissSnackbarState.reset()
                                    }
                                }
                            }
                        )
                        AppDialogs(uiState.dialogType, onEvent = viewModel::onEvent)
                    }
                }
            )
        }
    }
}
