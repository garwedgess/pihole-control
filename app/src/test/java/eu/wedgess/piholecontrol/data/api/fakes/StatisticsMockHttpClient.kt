package eu.wedgess.piholecontrol.data.api.fakes

import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object StatisticsMockHttpClient {

    private const val DIRECTORY = "statistics"

    private val queryTypes = loadJson(DIRECTORY, "query_types.json")
    private val forwardDestinations = loadJson(DIRECTORY, "forward_destinations.json")
    private val topClients = loadJson(DIRECTORY, "top_clients.json")
    private val topQueries = loadJson(DIRECTORY, "top_items.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val params = url.parameters

            when {
                params["getQueryTypes"] == "true" -> {
                    respond(
                        content = queryTypes,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["getForwardDestinations"] == "true" -> {
                    respond(
                        content = forwardDestinations,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["topItems"] == "true" -> {
                    respond(
                        content = topQueries,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["topClients"] == "true" -> {
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
            val params = url.parameters

            when {
                params["getQueryTypes"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["getForwardDestinations"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["topItems"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["topClients"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.BadRequest,
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