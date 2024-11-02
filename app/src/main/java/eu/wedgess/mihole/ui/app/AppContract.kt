package eu.wedgess.mihole.ui.app

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.enums.PiHoleStatus
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.app.model.AppDialogType
import eu.wedgess.mihole.ui.app.model.PiHoleAppInfo

interface AppContract {

    data class UiState(
        val appInfo: PiHoleAppInfo,
        val appBarState: AppBarState,
        val showConnectionDropdown: Boolean,
        val dialogType: AppDialogType
    ) {

        val adBlockingEnabled: Boolean get() = appInfo.status == PiHoleStatus.ENABLED

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
        data class OnConnectionSelected(val mihHole: PiHoleInfo) : Event
        data class SetDisabledStatus(val duration: Long) : Event
        data class UpdateAppBarState(val updateState: AppBarState) : Event
    }
}