package eu.wedgess.piholecontrol.presentation.connections.initial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.MainAppBar
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.connections.modify.ModifyConnectionsContract
import eu.wedgess.piholecontrol.presentation.connections.modify.view.ModifyConnectionScreen
import eu.wedgess.piholecontrol.presentation.connections.modify.viewmodel.ModifyConnectionViewModel
import eu.wedgess.piholecontrol.presentation.splash.SplashActivity
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.UiText

@AndroidEntryPoint
class InitialConnectionActivity : ComponentActivity() {

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = intent.extras?.getBoolean(EXTRA_KEY_IS_DARK_THEME) ?: false
            val isDynamicColors = intent.extras?.getBoolean(EXTRA_KEY_USE_DYNAMIC_COLORS) ?: false

            PiHoleControlTheme(
                darkTheme = isDarkTheme,
                dynamicColor = isDynamicColors
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        MainAppBar(
                            appBarState = AppBarState(
                                title = UiText.StringResource(R.string.initial_setup_title)
                            )
                        )
                    }
                ) { innerPadding ->
                    val viewModel: ModifyConnectionViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    CollectSideEffect(viewModel.sideEffect) { effect ->
                        when (effect) {
                            is ModifyConnectionsContract.Effect.Navigation.Back -> {
                                this@InitialConnectionActivity.finish()
                            }
                        }
                    }

                    Surface(modifier = Modifier.padding(innerPadding)) {
                        ModifyConnectionScreen(
                            uiState,
                            viewModel::onEvent
                        )
                    }
                }
            }
        }
    }

    companion object {
        internal const val EXTRA_KEY_IS_DARK_THEME = "EXTRA_KEY_IS_DARK_THEME"
        internal const val EXTRA_KEY_USE_DYNAMIC_COLORS = "EXTRA_KEY_USE_DYNAMIC_COLORS"
        fun getIntent(
            isDarkTheme: Boolean,
            useDynamicColors: Boolean,
            activity: SplashActivity
        ): Intent {
            return Intent(activity, InitialConnectionActivity::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putBoolean(EXTRA_KEY_IS_DARK_THEME, isDarkTheme)
                        putBoolean(EXTRA_KEY_USE_DYNAMIC_COLORS, useDynamicColors)
                    }
                )
            }
        }
    }
}
