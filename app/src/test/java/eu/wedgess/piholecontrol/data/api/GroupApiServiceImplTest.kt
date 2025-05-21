package eu.wedgess.piholecontrol.data.api

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.fakes.GroupsMockHttpClient
import eu.wedgess.piholecontrol.data.model.requests.PiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse
import eu.wedgess.piholecontrol.data.model.responses.PiHoleErrorResponseData
import eu.wedgess.piholecontrol.data.model.responses.exceptions.ApiErrorThrowable
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GroupApiServiceImplTest {

    private lateinit var defaultHttpClient: HttpClient
    private lateinit var trustAllCertsHttpClient: HttpClient
    private lateinit var target: GroupApiService

    @Before
    fun setup() {
        defaultHttpClient = GroupsMockHttpClient.mockSuccessHttpClient()
        trustAllCertsHttpClient = GroupsMockHttpClient.mockErrorHttpClient()
        target = GroupApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
    }

    @Test
    fun `fetchAllGroups - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.fetchAllGroups(connection)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `fetchAllGroups - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.fetchAllGroups(connection)

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }

    @Test
    fun `updateGroup - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.updateGroup(
            connection,
            name = "test.com",
            PiHoleGroupRequestData(
                name = "test.com",
                comment = "",
                enabled = true
            )
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `updateGroup - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.updateGroup(
            connection,
            name = "test.com",
            PiHoleGroupRequestData(
                name = "test.com",
                comment = "",
                enabled = true
            )
        )

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.notFound)
    }

    @Test
    fun `deleteGroup - returns success`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = false)

        val result = target.deleteGroup(
            connection = connection,
            name = "test.com"
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteGroup - returns failure`() = runTest {
        val connection = ConnectionEntity.default.copy(trustAllCerts = true)

        val result = target.deleteGroup(
            connection = connection,
            name = "test.com"
        )

        assertThat(result.isFailure).isTrue()
        val exception = result.exceptionOrNull()
        assertThat(exception).isInstanceOf(ApiErrorThrowable::class.java)
        assertThat((exception as ApiErrorThrowable).apiErrorResponse)
            .isInstanceOf(ApiErrorResponse::class.java)
        assertThat(exception.apiErrorResponse.serverError)
            .isEqualTo(PiHoleErrorResponseData.unauthorized)
    }
}
