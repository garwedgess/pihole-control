package eu.wedgess.piholecontrol.data.api.v6.fakes

import eu.wedgess.piholecontrol.data.api.loadJson
import eu.wedgess.piholecontrol.di.NetworkModule.installContentNegotiation
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object StatusMockHttpClientV6 {

    private val unauthorizedErrorData = loadJson("", "api_401_error_v6.json")

    fun mockSuccessHttpClient(): HttpClient {
        val mockEngine = MockEngine { request ->
            val body = request.body
            val method = request.method

            when {
                method == HttpMethod.Get -> {
                    respond(
                        content = """{
                          "blocking": "disabled",
                          "timer": null,
                          "took": 0.0000870227813720703
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                method == HttpMethod.Post && body.contentLength == 24L -> {
                    respond(
                        content = """{
                          "blocking": "enabled",
                          "timer": null,
                          "took": 0.0000870227813720703
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                method == HttpMethod.Post && body.contentLength == 25L -> {
                    respond(
                        content = """{
                          "blocking": "disabled",
                          "timer": null,
                          "took": 0.0000870227813720703
                        }""",
                        status = HttpStatusCode.OK,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }
                // Default failure: any other scenario
                else -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
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
            val body = request.body
            val method = request.method

            when {
                method == HttpMethod.Get -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                method == HttpMethod.Post && body.contentLength == 24L -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                method == HttpMethod.Post && body.contentLength == 25L -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
                        headers = headersOf("Content-Type", "application/json")
                    )
                }

                else -> {
                    respond(
                        content = unauthorizedErrorData,
                        status = HttpStatusCode.Unauthorized,
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
