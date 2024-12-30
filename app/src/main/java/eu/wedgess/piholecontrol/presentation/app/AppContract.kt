package eu.wedgess.piholecontrol.presentation.app

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.app.model.AppDialogType
import eu.wedgess.piholecontrol.presentation.app.model.NetworkStatusUiState
import eu.wedgess.piholecontrol.presentation.app.model.PiHoleAppInfo

interface AppContract {

    data class UiState(
        val appInfo: PiHoleAppInfo,
        val appBarState: AppBarState,
        val showConnectionDropdown: Boolean,
        val networkConnectionState: NetworkStatusUiState,
        val dialogType: AppDialogType
    ) {

        companion object {
            fun initial() = UiState(
                appInfo = PiHoleAppInfo.initial(),
                showConnectionDropdown = false,
                appBarState = AppBarState(),
                networkConnectionState = NetworkStatusUiState(),
                dialogType = AppDialogType.None
            )
        }
    }

    sealed interface Event {
        data object ShowEnabledStatusDialog : Event
        data object DismissDialog : Event
        data object ShowDisabledStatusDialog : Event
        data object SetEnabledStatus : Event
        data class OnConnectionSelected(val mihHole: ConnectionEntity) : Event
        data class SetDisabledStatus(val duration: Long) : Event
        data class UpdateAppBarState(val updateState: AppBarState) : Event
    }
}
