package eu.wedgess.piholecontrol.data.api.v6.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object AuthMockHttpClientV6 {

    private const val DIRECTORY = "auth/v6"

    private val auth = loadJson(DIRECTORY, "auth.json")
    private val unauthorizedErrorData = loadJson("", "api_401_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = auth,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }

    fun mockErrorHttpClient(): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = unauthorizedErrorData,
                status = HttpStatusCode.Unauthorized,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }
}
