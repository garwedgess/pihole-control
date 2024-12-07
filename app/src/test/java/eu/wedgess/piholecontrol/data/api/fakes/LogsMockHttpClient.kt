package eu.wedgess.piholecontrol.data.api.fakes

import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object LogsMockHttpClient {

    private const val DIRECTORY = "logs"

    private val logs = loadJson(DIRECTORY, "logs.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val params = url.parameters

            respond(
                content = logs,
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }

    fun mockErrorHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val params = url.parameters

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