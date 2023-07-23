package eu.wedgess.mihole.ui.app.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.viewmodel.AppViewModel
import eu.wedgess.mihole.ui.base.AppBarState
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.MainAppBar
import eu.wedgess.mihole.ui.navigation.MainNavigationGraph
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.navigation.bottom.BottomNavigationBar
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun MiHoleApp(
    viewModel: AppViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var appBarState by remember(uiState.currentConnection) {
        mutableStateOf(
            AppBarState(
                currentConnection = (uiState.currentConnection as? UiResult.Success)?.data
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(AppContract.Event.FetchSettings)
        viewModel.onEvent(AppContract.Event.FetchCurrentConnection)
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    MainAppBar(
                        appBarState = appBarState,
                        onNavigateBack = { navHostController.navigateUp() })
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
                    MainNavigationGraph(
                        modifier = Modifier.padding(contentPadding),
                        navController = navHostController,
                        onComposing = { updateState ->
                            appBarState = updateState.copy(currentConnection = (uiState.currentConnection as? UiResult.Success)?.data)
                        }
                    )
                }
            )
        }
    }
}