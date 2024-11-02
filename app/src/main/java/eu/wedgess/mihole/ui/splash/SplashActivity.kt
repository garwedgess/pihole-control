package eu.wedgess.mihole.ui.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.ui.app.view.MiHoleApp
import eu.wedgess.mihole.ui.compose.Compose
import eu.wedgess.mihole.ui.compose.ErrorScreen
import eu.wedgess.mihole.ui.compose.UIResult
import eu.wedgess.mihole.ui.splash.viewmodel.SplashViewModel
import eu.wedgess.mihole.ui.theme.MiHoleTheme

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
                    MiHoleApp(
                        isDarkTheme = isDarkTheme,
                        useDynamicColors = it.useDynamicColors
                    )
                }
            )
        }
    }
}