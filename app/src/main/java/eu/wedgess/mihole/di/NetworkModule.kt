package eu.wedgess.mihole.di

import android.annotation.SuppressLint
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.wedgess.mihole.data.utils.AllCertsTrustManager
import eu.wedgess.mihole.di.annotations.DefaultHttpClient
import eu.wedgess.mihole.di.annotations.TrustAllCertificatesHttpClient
import io.ktor.client.*
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.*
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

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient
        .Builder()
        .dns(object : Dns {
            override fun lookup(hostname: String): List<InetAddress> =
                Dns.SYSTEM.lookup(hostname).sortedByDescending { it is Inet4Address }
        }).build()

    @Provides
    @DefaultHttpClient
    @Singleton
    fun provideHttpClient(
        okHttpClient: OkHttpClient,
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
        okHttpClient: OkHttpClient,
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

    fun<T: HttpClientEngineConfig> HttpClientConfig<T>.installContentNegotiation() =
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

    fun<T: HttpClientEngineConfig> HttpClientConfig<T>.installLogging() = // custom logger set to use Timber
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.d(message)
                }
            }
            level = LogLevel.ALL
        }

    fun<T: HttpClientEngineConfig> HttpClientConfig<T>.installRedirect() = // custom logger set to use Timber
        install(HttpRedirect) {
            checkHttpMethod = false
        }


}