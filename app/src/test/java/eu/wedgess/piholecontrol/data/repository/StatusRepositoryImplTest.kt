package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.StatusApiServiceV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration

class StatusRepositoryImplTest {

    @MockK
    private lateinit var apiServiceV5: StatusApiServiceV5

    @MockK
    private lateinit var apiServiceV6: StatusApiServiceV6

    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: StatusRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = StatusRepositoryImpl(apiServiceV5, apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchStatus - apiV5 fetchStatus is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        coEvery { apiServiceV5.fetchStatus(activeConnection) } returns
                Result.success(mockk(relaxed = true))

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV5.fetchStatus(activeConnection) }
    }

    @Test
    fun `fetchStatus - apiV5 should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.fetchStatus(activeConnection) } returns Result.failure(exception)

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.fetchStatus(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - apiV5 enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        coEvery { apiServiceV5.enableAdBlocking(activeConnection) } returns Result.success(
            mockk(
                relaxed = true
            )
        )

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV5.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - apiV5 enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.enableAdBlocking(activeConnection) } returns Result.failure(exception)

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `disableAdBlocking - apiV5 enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        coEvery { apiServiceV5.disableAdBlocking(activeConnection, any()) } returns Result.success(
            mockk(
                relaxed = true
            )
        )

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV5.disableAdBlocking(activeConnection, any()) }
    }

    @Test
    fun `disableAdBlocking - apiV5 enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV5.disableAdBlocking(activeConnection, any()) } returns Result.failure(
            exception
        )

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV5.disableAdBlocking(activeConnection, any()) }
    }

    @Test
    fun `fetchStatus - apiV6 fetchStatus is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        coEvery { apiServiceV6.fetchStatus(activeConnection) } returns
                Result.success(mockk(relaxed = true))

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV6.fetchStatus(activeConnection) }
    }

    @Test
    fun `fetchStatus - apiV6 should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV6.fetchStatus(activeConnection) } returns Result.failure(exception)

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV6.fetchStatus(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - apiV6 enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        coEvery { apiServiceV6.enableAdBlocking(activeConnection) } returns Result.success(
            mockk(
                relaxed = true
            )
        )

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV6.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - apiV6 enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV6.enableAdBlocking(activeConnection) } returns Result.failure(exception)

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV6.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `disableAdBlocking - apiV6 enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        coEvery { apiServiceV6.disableAdBlocking(activeConnection, any()) } returns Result.success(
            mockk(
                relaxed = true
            )
        )

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { apiServiceV6.disableAdBlocking(activeConnection, any()) }
    }

    @Test
    fun `disableAdBlocking - apiV6 enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery { apiServiceV6.disableAdBlocking(activeConnection, any()) } returns Result.failure(
            exception
        )

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { apiServiceV6.disableAdBlocking(activeConnection, any()) }
    }
}
