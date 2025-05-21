package eu.wedgess.piholecontrol.data.api.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object FiltersMockHttpClient {

    private const val DIRECTORY = "filters/v6"

    private val list = loadJson(DIRECTORY, "list.json")
    private val add = loadJson(DIRECTORY, "add_success.json")
    private val unauthorizedErrorData = loadJson("", "api_401_error_v6.json")
    private val notFoundErrorData = loadJson("", "api_404_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val path = url.segments

            when (path.size) {
                3 -> {
                    respond(
                        content = list,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                4 -> {
                    respond(
                        content = add,
                        status = HttpStatusCode.Created,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                5 -> {
                    respond(
                        content = "",
                        status = HttpStatusCode.NoContent,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = """{"error": "Unknown error"}""",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
            }
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }

    fun mockErrorHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val path = url.segments

            when (path.size) {
                3 -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                4 -> {
                    respond(
                        content = notFoundErrorData,
                        status = HttpStatusCode.NotFound,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                5 -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.NotFound,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
            }
        }
        return HttpClient(mockEngine) {
            installContentNegotiation()
        }
    }
}
