package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.StatusApiService
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
    private lateinit var api: StatusApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: StatusRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = StatusRepositoryImpl(api, dispatcherProvider)
    }

    @Test
    fun `fetchStatus - api fetchStatus is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchStatus(activeConnection) } returns Result.success(mockk(relaxed = true))

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { api.fetchStatus(activeConnection) }
    }

    @Test
    fun `fetchStatus - should return failure on API error`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.fetchStatus(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.fetchStatus(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.fetchStatus(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - api enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.enableAdBlocking(activeConnection) } returns Result.success(mockk(relaxed = true))

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { api.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `enableAdBlocking - api enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.enableAdBlocking(activeConnection) } returns Result.failure(RuntimeException())

        val result = target.enableAdBlocking(activeConnection)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.enableAdBlocking(activeConnection) }
    }

    @Test
    fun `disableAdBlocking - api enableAdBlocking is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.disableAdBlocking(activeConnection, any()) } returns Result.success(mockk(relaxed = true))

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()).isInstanceOf(StatusEntity::class.java)
        coVerify { api.disableAdBlocking(activeConnection, any()) }
    }

    @Test
    fun `disableAdBlocking - api enableAdBlocking is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        coEvery { api.disableAdBlocking(activeConnection, any()) } returns Result.failure(RuntimeException())

        val result = target.disableAdBlocking(activeConnection, Duration.INFINITE)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isNotNull()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { api.disableAdBlocking(activeConnection, any()) }
    }
}