package eu.wedgess.piholecontrol.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import eu.wedgess.piholecontrol.domain.network.NetworkConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityObserverImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var currentNetworkState: NetworkConnectionState? = null

    private fun networkCallback(callback: (NetworkConnectionState) -> Unit): ConnectivityManager.NetworkCallback =
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Timber.d("GARETH --> onAvailable")
                val newNetworkState = NetworkConnectionState.Available
                if (currentNetworkState != newNetworkState) {
                    currentNetworkState = newNetworkState
                    Timber.d("GARETH --> onAvailable($newNetworkState)")
                    callback(newNetworkState)
                }
            }

            override fun onLost(network: Network) {
                Timber.d("GARETH --> onLost")
                val newNetworkState = NetworkConnectionState.Unavailable
                if (currentNetworkState != newNetworkState) {
                    currentNetworkState = newNetworkState
                    Timber.d("GARETH --> onLost($newNetworkState)")
                    callback(newNetworkState)
                }
            }

            override fun onUnavailable() {
                Timber.d("GARETH --> onUnavailable")
                val newNetworkState = NetworkConnectionState.Unavailable
                if (currentNetworkState != newNetworkState) {
                    currentNetworkState = newNetworkState
                    Timber.d("GARETH --> onUnavailable($newNetworkState)")
                    callback(newNetworkState)
                }
            }
        }

    private fun getCurrentConnectivityState(): NetworkConnectionState {
        val network = connectivityManager.activeNetwork
        return if (network != null) {
            NetworkConnectionState.Available
        } else {
            NetworkConnectionState.Unavailable
        }
    }

    override fun observe(): Flow<NetworkConnectionState> = callbackFlow {
        val callback = networkCallback { connectionState ->
            launch { send(connectionState) }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, callback)
        val currentState = getCurrentConnectivityState()
        if (currentNetworkState != currentState) {
            currentNetworkState = currentState
            launch { send(currentState) }
        }

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}