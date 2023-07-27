package eu.wedgess.mihole.ui.app.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.view.components.dialog.StatusDialog
import eu.wedgess.mihole.ui.app.viewmodel.AppViewModel
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.MainAppBar
import eu.wedgess.mihole.ui.navigation.MainNavigationGraph
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.navigation.bottom.BottomNavigationBar
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import kotlinx.coroutines.delay

@Composable
fun MiHoleApp(
    viewModel: AppViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    val backStackEntry = navHostController.currentBackStackEntryAsState()
    val systemUiController = rememberSystemUiController()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var appBarState by remember(uiState.currentConnection, uiState.status) {
        mutableStateOf(
            AppBarState(
                currentConnection = (uiState.currentConnection as? UiResult.Success)?.data,
                adBlockingEnabled = (uiState.status as? UiResult.Success)?.data == PiHoleStatus.ENABLED,
                connections = (uiState.connections as? UiResult.Success)?.data ?: emptyList()
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(AppContract.Event.FetchSettings)
        viewModel.onEvent(AppContract.Event.FetchCurrentConnection)
        viewModel.onEvent(AppContract.Event.FetchConnections)
    }

    DisposableEffect(Unit) {
        val refreshJob = viewModel.autoRefreshData()

        onDispose {
            refreshJob.cancel()
        }
    }

    val isDarkTheme = when (uiState.currentTheme) {
        UserPreferences.Theme.DARK -> true
        UserPreferences.Theme.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    MiHoleTheme(
        darkTheme = isDarkTheme,
        dynamicColor = uiState.useDynamicThemeColors
    ) {
        MaterialTheme.colorScheme.run {
            SideEffect {
                systemUiController.apply {
                    setStatusBarColor(this@run.background)
                    setNavigationBarColor(
                        this@run.surfaceColorAtElevation(
                            NavigationBarDefaults.Elevation
                        )
                    )
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    MainAppBar(
                        appBarState = appBarState,
                        onNavigateBack = { navHostController.navigateUp() },
                        onStatusClicked = { viewModel.onEvent(AppContract.Event.ShowStatusDialog) },
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
                        visible = backStackEntry.value?.destination?.route != Screens.Connections.route,
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = slideOutHorizontally(targetOffsetX = { -it }),
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
                },
                content = { contentPadding ->
                    AnimatedVisibility(visible = uiState.showStatusDialog) {
                        StatusDialog(
                            currentStatus = (uiState.status as? UiResult.Success)?.data
                                ?: PiHoleStatus.UNKNOWN,
                            onDisableStatus = { viewModel.onEvent(AppContract.Event.SetDisabledStatus(it))},
                            onEnabledStatus = { viewModel.onEvent(AppContract.Event.SetEnabledStatus) },
                            onDismissDialog = { viewModel.onEvent(AppContract.Event.DismissStatusDialog) }
                        )
                    }
                    MainNavigationGraph(
                        modifier = Modifier.padding(contentPadding),
                        navController = navHostController,
                        onComposing = { updateState ->
                            appBarState =
                                updateState.copy(
                                    currentConnection = (uiState.currentConnection as? UiResult.Success)?.data,
                                    adBlockingEnabled = (uiState.status as? UiResult.Success)?.data == PiHoleStatus.ENABLED,
                                    connections = (uiState.connections as? UiResult.Success)?.data
                                )
                        }
                    )
                }
            )
        }
    }
}