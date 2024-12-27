package eu.wedgess.piholecontrol.presentation.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.piholecontrol.domain.usecases.app.CheckHasConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import eu.wedgess.piholecontrol.presentation.compose.ResultType
import eu.wedgess.piholecontrol.presentation.compose.UIResult
import eu.wedgess.piholecontrol.presentation.settings.model.mapToAppThemePres
import eu.wedgess.piholecontrol.presentation.splash.model.SplashInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    fetchAppPreferencesUseCase: FetchAppPreferencesUseCase,
    private val checkHasConnectionsUseCase: CheckHasConnectionsUseCase
) : ViewModel() {

    val splashInfo = fetchAppPreferencesUseCase().map {
        UIResult.Loaded(
            SplashInfo(
                theme = it.theme.mapToAppThemePres(),
                useDynamicColors = it.useDynamicColors,
                hasConnections = checkHasConnectionsUseCase().getOrDefault(false)
            )
        )
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UIResult.Loading(ResultType.Loading.WithTitle())
        )
}
