package eu.wedgess.piholecontrol.presentation.app.model

import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState

data class NetworkStatusUiState(
    val networkConnectionState: NetworkConnectionState = NetworkConnectionState.Available,
    val isVisible: Boolean = false
) {
    val isConnected: Boolean get() = this.networkConnectionState == NetworkConnectionState.Available
}
