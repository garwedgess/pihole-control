package eu.wedgess.piholecontrol.data.api.v5.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object DashboardMockHttpClientV5 {

    private const val DIRECTORY = "dashboard/v5"

    private val summary = loadJson(DIRECTORY, "summary.json")
    private val overTimeData10mins = loadJson(DIRECTORY, "overtime_data_10mins.json")
    private val clientOverTimeData = loadJson(DIRECTORY, "overtime_clients.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val params = url.parameters

            when {
                params["summaryRaw"] == "true" -> {
                    respond(
                        content = summary,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeData10mins"] == "true" -> {
                    respond(
                        content = overTimeData10mins,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeDataClients"] == "true" -> {
                    respond(
                        content = clientOverTimeData,
                        status = HttpStatusCode.OK,
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
            val params = url.parameters

            when {
                params["summaryRaw"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeData10mins"] == "true" -> {
                    respond(
                        content = """[]""",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeDataClients"] == "true" -> {
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
