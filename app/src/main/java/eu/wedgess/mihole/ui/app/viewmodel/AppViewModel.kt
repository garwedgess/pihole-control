package eu.wedgess.mihole.ui.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.ResponseResult
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.app.AppContract
import eu.wedgess.mihole.ui.app.controller.AppController
import eu.wedgess.mihole.ui.app.model.AppDialogType
import eu.wedgess.mihole.ui.base.EventDrivenViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModel
import eu.wedgess.mihole.ui.base.SideEffectViewModelImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val controller: AppController
) : ViewModel(),
    EventDrivenViewModel<AppContract.Event>,
    SideEffectViewModel<AppContract.Effect> by SideEffectViewModelImpl() {

    private val _uiState = MutableStateFlow(AppContract.UiState.initial())

    val uiState = controller.fetchAppInfo().combine(_uiState) { appInfo, uiState ->
        uiState.copy(
            appInfo = appInfo,
            appBarState = uiState.appBarState.copy(
                adBlockingEnabled = appInfo.status == PiHoleStatus.ENABLED,
                currentConnection = appInfo.currentConnection,
                connections = appInfo.connections,
            )
        )
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            AppContract.UiState.initial()
        )


    override fun onEvent(event: AppContract.Event) {
        when (event) {
            AppContract.Event.DismissDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.None)
            }

            is AppContract.Event.SetDisabledStatus -> setDisableAdBlocking(event.duration)
            is AppContract.Event.OnConnectionSelected -> setConnectionActive(event.mihHole)
            AppContract.Event.SetEnabledStatus -> setEnableAdBlocking()
            AppContract.Event.ShowEnabledStatusDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.EnableAdBlocking)
            }

            AppContract.Event.ShowDisabledStatusDialog -> _uiState.update {
                it.copy(dialogType = AppDialogType.DisableAdBlocking)
            }

            is AppContract.Event.UpdateAppBarState -> _uiState.update {
                it.copy(appBarState = event.updateState)
            }
        }
    }

    private fun setConnectionActive(mihHole: PiHoleInfo) {
        viewModelScope.launch {
            controller.setConnectionAsActive(mihHole.id)
                .onFailure { Timber.e("Failed to change active connection", it) }
        }
    }


    private fun setDisableAdBlocking(long: Long) {
        _uiState.update { it.copy(dialogType = AppDialogType.None) }
        viewModelScope.launch {
            controller.disableAdBlocking(long)
                .onFailure {  }
                .onSuccess {  }
        }
    }

    private fun setEnableAdBlocking() {
        _uiState.update { it.copy(dialogType = AppDialogType.None) }
        viewModelScope.launch {
            controller.enableAdBlocking()
                .onFailure {  }
                .onSuccess {  }
        }
    }
}