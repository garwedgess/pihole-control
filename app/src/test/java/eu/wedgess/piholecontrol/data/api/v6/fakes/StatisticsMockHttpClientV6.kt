package eu.wedgess.piholecontrol.data.api.v6.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object StatisticsMockHttpClientV6 {

    private const val DIRECTORY = "statistics/v6"

    private val queryTypes = loadJson(DIRECTORY, "query_types.json")
    private val forwardDestinations = loadJson(DIRECTORY, "forward_destinations.json")
    private val topClients = loadJson(DIRECTORY, "top_clients.json")
    private val topQueries = loadJson(DIRECTORY, "top_items.json")
    private val unauthorizedErrorData = loadJson("", "api_401_error_v6.json")
    private val notFoundErrorData = loadJson("", "api_404_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val path = url.segments

            when {
                path.contains("query_types") -> {
                    respond(
                        content = queryTypes,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("upstreams") -> {
                    respond(
                        content = forwardDestinations,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("top_domains") -> {
                    respond(
                        content = topQueries,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("top_clients") -> {
                    respond(
                        content = topClients,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

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

            when {
                path.contains("query_types") -> {
                    respond(
                        content = notFoundErrorData,
                        status = HttpStatusCode.NotFound,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("upstreams") -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("top_domains") -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                path.contains("top_clients") -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = notFoundErrorData,
                        status = HttpStatusCode.NotFound,
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
