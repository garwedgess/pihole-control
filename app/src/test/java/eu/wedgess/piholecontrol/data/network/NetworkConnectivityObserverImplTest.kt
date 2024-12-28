package eu.wedgess.piholecontrol.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.NetworkConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowConnectivityManager
import org.robolectric.shadows.ShadowNetwork
import org.robolectric.shadows.ShadowNetworkCapabilities

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], shadows = [ShadowNetworkCapabilities::class])
class NetworkConnectivityObserverImplTest {

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var shadowConnectivityManager: ShadowConnectivityManager
    private lateinit var networkConnectivityObserver: NetworkConnectivityObserverImpl
    private lateinit var context: Context
    private lateinit var network: Network

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowConnectivityManager = Shadows.shadowOf(connectivityManager)
        networkConnectivityObserver = NetworkConnectivityObserverImpl(context)
        network = ShadowNetwork.newInstance(1)
    }

    @Test
    fun `GIVEN no active network WHEN observe is called THEN emit Unavailable`() = runTest {
        shadowConnectivityManager.setNetworkCapabilities(network, null)
        shadowConnectivityManager.setDefaultNetworkActive(false)

        // When
        networkConnectivityObserver.observe().test {
            // Then
            shadowConnectivityManager.networkCallbacks.forEach {
                it.onLost(network)
            }
            assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Unavailable)
            expectNoEvents()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `GIVEN active wifi network WHEN observe is called THEN emit Available`() = runTest {
        // Given
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        Shadows.shadowOf(networkCapabilities).apply {
            addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        shadowConnectivityManager.setNetworkCapabilities(network, networkCapabilities)
        shadowConnectivityManager.setDefaultNetworkActive(true)

        // When
        networkConnectivityObserver.observe().test {
            // Then
            shadowConnectivityManager.networkCallbacks.forEach {
                it.onAvailable(network)
            }
            assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Available)
            expectNoEvents()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `GIVEN active cellular network WHEN observe is called THEN emit Available`() = runTest {
        // Given
        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        Shadows.shadowOf(networkCapabilities).apply {
            addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        shadowConnectivityManager.setNetworkCapabilities(network, networkCapabilities)
        shadowConnectivityManager.setDefaultNetworkActive(true)

        // When
        networkConnectivityObserver.observe().test {
            // Then
            shadowConnectivityManager.networkCallbacks.forEach {
                it.onAvailable(network)
            }
            assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Available)
            expectNoEvents()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `GIVEN network becomes available WHEN network callback is triggered THEN emit Available`() =
        runTest {
            // Given
            val networkCapabilities = ShadowNetworkCapabilities.newInstance()
            Shadows.shadowOf(networkCapabilities).apply {
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            shadowConnectivityManager.setDefaultNetworkActive(false)

            // When
            networkConnectivityObserver.observe().test {
                // Then
                shadowConnectivityManager.networkCallbacks.forEach {
                    it.onLost(network)
                }
                assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Unavailable)
                shadowConnectivityManager.setNetworkCapabilities(network, networkCapabilities)
                shadowConnectivityManager.setDefaultNetworkActive(true)
                shadowConnectivityManager.networkCallbacks.forEach {
                    it.onAvailable(network)
                }
                assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Available)
                expectNoEvents()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `GIVEN network becomes unavailable WHEN network callback is triggered THEN emit Unavailable`() =
        runTest {
            // Given
            val networkCapabilities = ShadowNetworkCapabilities.newInstance()
            Shadows.shadowOf(networkCapabilities).apply {
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            shadowConnectivityManager.setNetworkCapabilities(network, networkCapabilities)
            shadowConnectivityManager.setDefaultNetworkActive(true)

            // When
            networkConnectivityObserver.observe().test {
                // Then
                shadowConnectivityManager.networkCallbacks.forEach {
                    it.onAvailable(network)
                }
                assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Available)
                shadowConnectivityManager.setDefaultNetworkActive(false)
                shadowConnectivityManager.networkCallbacks.forEach {
                    it.onLost(network)
                }
                assertThat(awaitItem()).isEqualTo(NetworkConnectionState.Unavailable)
                expectNoEvents()
                cancelAndConsumeRemainingEvents()
            }
        }
}
