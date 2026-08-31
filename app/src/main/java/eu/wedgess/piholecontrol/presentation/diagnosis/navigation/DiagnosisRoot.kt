package eu.wedgess.piholecontrol.presentation.diagnosis.navigation

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.model.SelectionMode
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

        LaunchedEffect(uiState.selectionMode) {
            val selectionMode = uiState.selectionMode
            onComposing(
                if (selectionMode is SelectionMode.Active) {
                    AppBarState.Contextual(
                        selectedCount = selectionMode.count,
                        title = UiText.StringResourceWithArgs(
                            R.string.all_selected_count,
                            selectionMode.count
                        ),
                        onDismiss = {
                            viewModel.onEvent(DiagnosisContract.Event.OnClearSelection)
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(
                                        DiagnosisContract.Event
                                            .OnDismissSelectedDiagnosisMessagesClick
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = stringResource(
                                        R.string.all_cd_dismiss_selected
                                    )
                                )
                            }
                        }
                    )
                } else {
                    AppBarState.Normal(
                        title = UiText.StringResource(id = R.string.nav_title_diagnosis),
                        showNavigateBackIcon = true,
                        displayConnection = false,
                        bottomBarVisible = false
                    )
                }
            )
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
