package eu.wedgess.piholecontrol.presentation.app

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.app.model.AppDialogType
import eu.wedgess.piholecontrol.presentation.app.model.PiHoleAppInfo

interface AppContract {

    data class UiState(
        val appInfo: PiHoleAppInfo,
        val appBarState: AppBarState,
        val showConnectionDropdown: Boolean,
        val dialogType: AppDialogType
    ) {

        val adBlockingEnabled: Boolean get() = appInfo.status == StatusEntity.ENABLED

        companion object {
            fun initial() = UiState(
                appInfo = PiHoleAppInfo.initial(),
                showConnectionDropdown = false,
                appBarState = AppBarState(),
                dialogType = AppDialogType.None
            )
        }
    }

    sealed interface Effect {
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