package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.network.NetworkConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveNetworkConnectivityUseCase @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver
) {
    operator fun invoke(): Flow<NetworkConnectionState> = networkConnectivityObserver.observe()
}
