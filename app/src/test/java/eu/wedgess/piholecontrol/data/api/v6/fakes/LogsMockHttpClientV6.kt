package eu.wedgess.piholecontrol.data.api.v6.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object LogsMockHttpClientV6 {

    private const val DIRECTORY = "logs/v6"

    private val logs = loadJson(DIRECTORY, "logs.json")
    private val logSuggestions = loadJson(DIRECTORY, "suggestions.json")
    private val notFoundErrorData = loadJson("", "api_404_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val path = url.segments
            when (path.size) {
                2 -> respond(
                    content = logs,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "application/json")
                )

                // size = 3 for suggestions
                else -> respond(
                    content = logSuggestions,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "application/json")
                )
            }
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }

    fun mockErrorHttpClient(): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = notFoundErrorData,
                status = HttpStatusCode.NotFound,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }
}
