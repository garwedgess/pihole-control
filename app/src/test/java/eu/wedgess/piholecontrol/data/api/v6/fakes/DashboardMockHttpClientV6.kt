package eu.wedgess.piholecontrol.data.api.v6.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object DashboardMockHttpClientV6 {

    private const val DIRECTORY = "dashboard/v6"

    private val summary = loadJson(DIRECTORY, "summary.json")
    private val overTimeData10mins = loadJson(DIRECTORY, "overtime_data_10mins.json")
    private val clientOverTimeData = loadJson(DIRECTORY, "overtime_clients.json")
    private val unauthorizedErrorData = loadJson("", "api_401_error_v6.json")
    private val notFoundErrorData = loadJson("", "api_404_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url
            val pathList = url.segments

            when {
                pathList.contains("summary") -> {
                    respond(
                        content = summary,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                pathList.contains("history") && !pathList.contains("clients") -> {
                    respond(
                        content = overTimeData10mins,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                pathList.containsAll(listOf("history", "clients")) -> {
                    respond(
                        content = clientOverTimeData,
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = unauthorizedErrorData,
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
                        content = notFoundErrorData,
                        status = HttpStatusCode.NotFound,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeData10mins"] == "true" -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                params["overTimeDataClients"] == "true" -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.BadRequest,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = unauthorizedErrorData,
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
