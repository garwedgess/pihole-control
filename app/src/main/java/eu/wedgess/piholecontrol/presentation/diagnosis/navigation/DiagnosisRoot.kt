package eu.wedgess.piholecontrol.presentation.diagnosis.navigation

import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.compose.CollectSideEffect
import eu.wedgess.piholecontrol.presentation.diagnosis.DiagnosisContract
import eu.wedgess.piholecontrol.presentation.diagnosis.view.DiagnosisScreen
import eu.wedgess.piholecontrol.presentation.diagnosis.viewmodel.DiagnosisViewModel
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

fun NavGraphBuilder.diagnosisRoot(
    onComposing: (AppBarState) -> Unit
) {
    composable<Screens.Diagnosis> {
        val viewModel: DiagnosisViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val appBarState = remember {
            AppBarState(
                title = UiText.StringResource(id = R.string.nav_title_diagnosis),
                showNavigateBackIcon = true,
                displayConnection = false,
                bottomBarVisible = false
            )
        }

        LaunchedEffect(appBarState) {
            onComposing(appBarState)
        }

        CollectSideEffect(viewModel.sideEffect) { effect ->
            when (effect) {
                is DiagnosisContract.Effect.Toast -> {
                    Toast.makeText(
                        context,
                        effect.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        DiagnosisScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent
        )
    }
}
