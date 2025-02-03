@file:Suppress("DEPRECATION")

package eu.wedgess.piholecontrol.di

import android.annotation.SuppressLint
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.network.SessionIdAuthenticator
import eu.wedgess.piholecontrol.data.repository.TokenRefresher
import eu.wedgess.piholecontrol.data.utils.AllCertsTrustManager
import eu.wedgess.piholecontrol.di.annotations.AuthHttpClient
import eu.wedgess.piholecontrol.di.annotations.AuthOkHttpClient
import eu.wedgess.piholecontrol.di.annotations.AuthTrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TokenRefreshOkHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import timber.log.Timber
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress
import javax.inject.Singleton
import javax.net.ssl.SSLContext

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val ip4PreferredDns = object : Dns {
        override fun lookup(hostname: String): List<InetAddress> =
            Dns.SYSTEM.lookup(hostname).sortedByDescending { it is Inet4Address }
    }

    @Provides
    @Singleton
    fun provideSessionIdAuthenticator(
        tokenRefresher: TokenRefresher,
        connectionRepository: ConnectionRepository
    ): SessionIdAuthenticator = SessionIdAuthenticator(tokenRefresher, connectionRepository)

    @Provides
    @AuthOkHttpClient
    @Singleton
    fun provideAuthOkHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .dns(ip4PreferredDns)
        .protocols(listOf(Protocol.HTTP_1_1))
        .build()

    @Provides
    @TokenRefreshOkHttpClient
    @Singleton
    fun provideOkHttpClient(
        sessionIdAuthenticator: SessionIdAuthenticator,
    ): OkHttpClient = OkHttpClient
        .Builder()
        .authenticator(sessionIdAuthenticator)
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
        )
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
        )
        .protocols(listOf(Protocol.HTTP_1_1))
        .dns(ip4PreferredDns)
        .build()

    @Provides
    @AuthHttpClient
    @Singleton
    fun provideAuthHttpClient(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    cache(Cache(File(context.cacheDir, "ktor"), 10 * 1024 * 1024))
                }
                preconfigured = okHttpClient
            }
            installContentNegotiation()
            installLogging()
            installRedirect()
        }
    }

    @SuppressLint("AllowAllHostnameVerifier")
    @Provides
    @AuthTrustAllCertificatesHttpClient
    @Singleton
    fun provideAuthAllowSelfSignedCertsHttpClient(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpClient {
        val trustManager = AllCertsTrustManager()
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        return HttpClient(OkHttp) {
            engine {
                config {
                    cache(Cache(File(context.cacheDir, "ktor"), 10 * 1024 * 1024))
                }
                preconfigured = okHttpClient.newBuilder()
                    .sslSocketFactory(
                        sslSocketFactory = sslContext.socketFactory,
                        trustManager = trustManager
                    )
                    .hostnameVerifier(AllowAllHostnameVerifier())
                    .build()
            }
            installContentNegotiation()
            installLogging()
            installRedirect()
        }
    }

    @Provides
    @DefaultHttpClient
    @Singleton
    fun provideHttpClient(
        @TokenRefreshOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    cache(Cache(File(context.cacheDir, "ktor"), 10 * 1024 * 1024))
                }
                preconfigured = okHttpClient
            }
            installContentNegotiation()
            installLogging()
            installRedirect()
        }
    }

    @SuppressLint("AllowAllHostnameVerifier")
    @Provides
    @TrustAllCertificatesHttpClient
    @Singleton
    fun provideAllowSelfSignedCertsHttpClient(
        @TokenRefreshOkHttpClient okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ): HttpClient {
        val trustManager = AllCertsTrustManager()
        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        return HttpClient(OkHttp) {
            engine {
                config {
                    cache(Cache(File(context.cacheDir, "ktor"), 10 * 1024 * 1024))
                    retryOnConnectionFailure(true)
                }
                preconfigured = okHttpClient.newBuilder()
                    .sslSocketFactory(
                        sslSocketFactory = sslContext.socketFactory,
                        trustManager = trustManager
                    )
                    .hostnameVerifier(AllowAllHostnameVerifier())
                    .build()
            }
            installContentNegotiation()
            installLogging()
            installRedirect()
        }
    }

    fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installContentNegotiation() =
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

    private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installLogging() =
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.d(message)
                }
            }
            level = LogLevel.ALL
        }

    private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installRedirect() =
        install(HttpRedirect) {
            checkHttpMethod = false
        }
}
