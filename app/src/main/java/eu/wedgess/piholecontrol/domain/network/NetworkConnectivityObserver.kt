package eu.wedgess.piholecontrol.domain.network

import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityObserver {
    fun observe(): Flow<NetworkConnectionState>
}
