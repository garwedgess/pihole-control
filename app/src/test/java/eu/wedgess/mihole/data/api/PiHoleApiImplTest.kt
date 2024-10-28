//package eu.wedgess.mihole.data.api
//
//import eu.wedgess.mihole.data.model.PiHoleInfo
//import eu.wedgess.mihole.data.model.responses.PiHoleStatusResponse
//import eu.wedgess.mihole.data.model.enums.PiHoleStatus
//import eu.wedgess.mihole.data.utils.errorBody
//import eu.wedgess.mihole.data.utils.handleResponse
//import eu.wedgess.mihole.data.utils.requestResult
//import eu.wedgess.mihole.di.annotations.DefaultHttpClient
//import eu.wedgess.mihole.di.annotations.TrustAllCertificatesHttpClient
//import io.ktor.client.HttpClient
//import io.ktor.client.call.body
//import io.ktor.client.engine.mock.MockEngine
//import io.ktor.client.engine.mock.respond
//import io.ktor.client.engine.mock.respondError
//import io.ktor.client.plugins.ClientRequestException
//import io.ktor.client.plugins.HttpResponseValidator
//import io.ktor.client.plugins.ResponseException
//import io.ktor.client.plugins.ServerResponseException
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.request.HttpRequestBuilder
//import io.ktor.client.request.request
//import io.ktor.client.statement.HttpResponse
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.headersOf
//import io.ktor.serialization.kotlinx.json.json
//import io.ktor.utils.io.ByteReadChannel
//import io.mockk.MockKAnnotations
//import io.mockk.coEvery
//import io.mockk.coJustRun
//import io.mockk.coVerify
//import io.mockk.every
//import io.mockk.impl.annotations.MockK
//import io.mockk.impl.annotations.RelaxedMockK
//import io.mockk.justRun
//import io.mockk.mockk
//import io.mockk.slot
//import io.mockk.spyk
//import io.mockk.verify
//import junit.framework.TestCase.assertEquals
//import junit.framework.TestCase.assertTrue
//import kotlinx.coroutines.test.runTest
//import kotlinx.serialization.json.Json
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//
//class PiHoleApiImplTest {
//
//    @RelaxedMockK
//    @DefaultHttpClient
//    private lateinit var defaultHttpClient: HttpClient
//
//    @TrustAllCertificatesHttpClient
//    @RelaxedMockK
//    private lateinit var trustAllCertsHttpClient: HttpClient
//
//    private lateinit var target: PiHoleApiImpl
//
//    @Before
//    fun setup() {
//        MockKAnnotations.init(this)
//        target = spyk(PiHoleApiImpl(defaultHttpClient, trustAllCertsHttpClient))
//    }
//
//    @Test
//    fun `fetchStatus should use trustAllCertsHttpClient when trustAllCerts is true`() = runTest {
//        // Arrange
//        val activeMiHole = PiHoleInfo.default.copy(trustAllCerts = true)
//        val expectedResponse = PiHoleStatusResponse(status = PiHoleStatus.ENABLED)
//        coEvery { trustAllCertsHttpClient.requestResult<PiHoleStatusResponse, String>(any()) } returns Result.success(expectedResponse)
//
//
//        // Act
//        val result = target.fetchStatus(activeMiHole)
//
//
//        // Assert
//        assertTrue(result.isSuccess)
//        assertEquals(expectedResponse, result.getOrThrow())
//
//        // Verify that piHoleRequestResult was called
//        coVerify { trustAllCertsHttpClient.requestResult<PiHoleStatusResponse, String>(any()) }
//    }
//
//
//
//    /**
//     * Create mock HttpClient for testing
//     *
//     * @param responseData - response data to be returned by client
//     * @param responseStatusCode - status code to test HttpError
//     * @return our mocked HttpClient
//     */
//    private fun createMockClient(
//        responseData: String,
//        responseStatusCode: HttpStatusCode = HttpStatusCode.OK
//    ): HttpClient {
//        val mockEngine = MockEngine {
//            if (responseStatusCode == HttpStatusCode.OK) {
//                respond(
//                    content = ByteReadChannel(responseData),
//                    status = responseStatusCode,
//                    headers = headersOf(
//                        HttpHeaders.ContentType,
//                        ContentType.Application.Json.toString()
//                    )
//                )
//            } else {
//                respondError(
//                    content = responseData,
//                    status = responseStatusCode,
//                    headers = headersOf(HttpHeaders.ContentType, ContentType.Text.Plain.toString())
//                )
//            }
//        }
//        return HttpClient(mockEngine) {
//            install(ContentNegotiation) {
//                json(Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//            }
//            HttpResponseValidator {
//                validateResponse { response: HttpResponse ->
//                    when (val statusCode = response.status.value) {
//                        in HttpStatusCode.BadRequest.value..HttpStatusCode.TooManyRequests.value -> {
//                            throw ClientRequestException(response, "Client error: HTTP $statusCode")
//                        }
//
//                        in HttpStatusCode.InternalServerError.value..HttpStatusCode.InsufficientStorage.value -> {
//                            throw ServerResponseException(response, "Server error: HTTP $statusCode")
//                        }
//                        HttpStatusCode.NoContent.value -> throw Exception("No content: HTTP $statusCode")
//                        HttpStatusCode.OK.value, HttpStatusCode.Created.value -> response.body()
//                        else ->  throw ResponseException(response, "Unexpected HTTP status: $statusCode")
//                    }
//                }
//                handleResponseExceptionWithRequest { exception, _ ->
//                    throw exception
//                }
//            }
//        }
//    }
//
//
//}