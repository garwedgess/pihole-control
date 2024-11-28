package eu.wedgess.piholecontrol.presentation.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.presentation.app.view.PiHoleControlApp
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.splash.viewmodel.SplashViewModel

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel = hiltViewModel<SplashViewModel>()
            val uiResult by viewModel.userPreferences.collectAsStateWithLifecycle()

            val splashScreen = installSplashScreen()

            // Set the condition to keep the splash screen on screen
            splashScreen.setKeepOnScreenCondition { uiResult is UIResult.Loading }

            uiResult.Compose(
                onLoaded = {
                    val isDarkTheme = when (it.theme) {
                        UserPreferences.Theme.DARK -> true
                        UserPreferences.Theme.LIGHT -> false
                        else -> isSystemInDarkTheme()
                    }
                    PiHoleControlApp(
                        isDarkTheme = isDarkTheme,
                        useDynamicColors = it.useDynamicColors
                    )
                }
            )
        }
    }
}