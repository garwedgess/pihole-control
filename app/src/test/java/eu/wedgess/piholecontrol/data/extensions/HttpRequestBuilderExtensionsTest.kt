package eu.wedgess.piholecontrol.data.extensions

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.URLProtocol
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HttpRequestBuilderExtensionsTest {

    @Test
    fun `fetchBaseRequestInfo - sets URL correctly without auth`() = runTest {
        val connection = ConnectionEntity(
            id = 1,
            name = "test",
            protocol = URLProtocol.HTTPS,
            host = "my-pihole.local",
            port = 8080,
            apiPath = "/admin/api.php",
            token = "my-token",
            trustAllCerts = false,
            authUsername = "",
            authPassword = "",
            authRealm = "",
            isDeleted = false,
            isActive = true
        )
        val requestBuilder = HttpRequestBuilder()

        requestBuilder.fetchBaseRequestInfoV5(connection)
        val requestData = requestBuilder.build()

        assertThat(requestData.url.protocol).isEqualTo(URLProtocol.HTTPS)
        assertThat(requestData.url.host).isEqualTo("my-pihole.local")
        assertThat(requestData.url.port).isEqualTo(8080)
        assertThat(requestData.url.encodedPath).isEqualTo("/admin/api.php")
        assertThat(requestData.url.parameters["auth"]).isEqualTo("my-token")
        assertThat(requestData.headers.entries()).isEmpty()
    }

    @Test
    fun `fetchBaseRequestInfo - sets URL correctly with auth`() = runTest {
        val connection = ConnectionEntity(
            id = 1,
            name = "test",
            protocol = URLProtocol.HTTP,
            host = "192.168.1.10",
            port = 80,
            apiPath = "/admin/api.php",
            token = "",
            trustAllCerts = false,
            authUsername = "admin",
            authPassword = "password",
            authRealm = "MyRealm",
            isDeleted = false,
            isActive = true
        )
        val requestBuilder = HttpRequestBuilder()

        requestBuilder.fetchBaseRequestInfoV5(connection)
        val requestData = requestBuilder.build()

        assertThat(requestData.url.protocol).isEqualTo(URLProtocol.HTTP)
        assertThat(requestData.url.host).isEqualTo("192.168.1.10")
        assertThat(requestData.url.port).isEqualTo(80)
        assertThat(requestData.url.encodedPath).isEqualTo("/admin/api.php")
        assertThat(requestData.url.parameters.entries()).isEmpty()
        assertThat(requestData.headers["Authorization"]).isEqualTo("Basic YWRtaW46cGFzc3dvcmQ=")
    }

    @Test
    fun `fetchBaseRequestInfo - sets URL correctly with auth and empty realm`() = runTest {
        val connection = ConnectionEntity(
            id = 1,
            name = "test",
            protocol = URLProtocol.HTTP,
            host = "192.168.1.10",
            port = 80,
            apiPath = "/admin/api.php",
            token = "",
            trustAllCerts = false,
            authUsername = "admin",
            authPassword = "password",
            authRealm = "",
            isDeleted = false,
            isActive = true
        )
        val requestBuilder = HttpRequestBuilder()

        requestBuilder.fetchBaseRequestInfoV5(connection)
        val requestData = requestBuilder.build()

        assertThat(requestData.url.protocol).isEqualTo(URLProtocol.HTTP)
        assertThat(requestData.url.host).isEqualTo("192.168.1.10")
        assertThat(requestData.url.port).isEqualTo(80)
        assertThat(requestData.url.encodedPath).isEqualTo("/admin/api.php")
        assertThat(requestData.url.parameters.entries()).isEmpty()
        assertThat(requestData.headers["Authorization"]).isEqualTo("Basic YWRtaW46cGFzc3dvcmQ=")
    }

    @Test
    fun `fetchBaseRequestInfo - sets URL correctly with empty token`() = runTest {
        val connection = ConnectionEntity(
            id = 1,
            name = "test",
            protocol = URLProtocol.HTTP,
            host = "192.168.1.10",
            port = 80,
            apiPath = "/admin/api.php",
            token = "",
            trustAllCerts = false,
            authUsername = "",
            authPassword = "",
            authRealm = "",
            isDeleted = false,
            isActive = true
        )
        val requestBuilder = HttpRequestBuilder()

        requestBuilder.fetchBaseRequestInfoV5(connection)
        val requestData = requestBuilder.build()

        assertThat(requestData.url.protocol).isEqualTo(URLProtocol.HTTP)
        assertThat(requestData.url.host).isEqualTo("192.168.1.10")
        assertThat(requestData.url.port).isEqualTo(80)
        assertThat(requestData.url.encodedPath).isEqualTo("/admin/api.php")
        assertThat(requestData.url.parameters.entries()).isEmpty()
        assertThat(requestData.headers.entries()).isEmpty()
    }
}
