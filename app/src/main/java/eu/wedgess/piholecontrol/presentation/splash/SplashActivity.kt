package eu.wedgess.piholecontrol.presentation.splash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.piholecontrol.presentation.app.view.PiHoleControlApp
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.connections.initial.InitialConnectionActivity
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.splash.viewmodel.SplashViewModel

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<SplashViewModel>()
            val uiResult by viewModel.splashInfo.collectAsStateWithLifecycle()

            val splashScreen = installSplashScreen()

            splashScreen.setKeepOnScreenCondition { uiResult is UIResult.Loading }

            uiResult.Compose(
                onLoaded = {
                    val isDarkTheme = when (it.theme) {
                        AppThemePres.Dark -> true
                        AppThemePres.Light -> false
                        else -> isSystemInDarkTheme()
                    }
                    PiHoleControlApp(
                        isDarkTheme = isDarkTheme,
                        useDynamicColors = it.useDynamicColors
                    )
                    if (!it.hasConnections) {
                        startActivity(
                            InitialConnectionActivity.getIntent(
                                isDarkTheme = isDarkTheme,
                                useDynamicColors = it.useDynamicColors,
                                activity = this@SplashActivity
                            )
                        )
                    }
                }
            )
        }
    }
}
