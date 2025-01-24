package eu.wedgess.piholecontrol.data.api.v5.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object LogsMockHttpClientV5 {

    private const val DIRECTORY = "logs/v5"

    private val logs = loadJson(DIRECTORY, "logs.json")
    private val clients = loadJson(DIRECTORY, "suggestions_client.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val params = url.parameters

            if (params.contains("getClientNames")) {
                respond(
                    content = clients,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type", "application/json")
                )
            } else {
                respond(
                    content = logs,
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
        val mockEngine = MockEngine { request ->
            val url = request.url

            respond(
                content = """[]""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }
}
