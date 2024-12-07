package eu.wedgess.piholecontrol.domain.usecase.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateRefreshIntervalUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateRefreshIntervalUseCaseTest {

    @MockK
    private lateinit var repository: SettingsRepository
    private lateinit var target: UpdateRefreshIntervalUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = UpdateRefreshIntervalUseCase(repository)
    }

    @Test
    fun `invoke - successfully updates refresh interval`() = runTest {
        val interval = 5000L
        coEvery { repository.updateRefreshInterval(interval) } returns Result.success(Unit)

        val result = target(interval)

        assertThat(result.isSuccess).isTrue()
        coEvery { repository.updateRefreshInterval(interval) }
    }

    @Test
    fun `invoke - returns failure when repository fails to update refresh interval`() = runTest {
        val interval = 5000L
        val exception = Exception("Failed to update refresh interval")
        coEvery { repository.updateRefreshInterval(interval) } returns Result.failure(exception)

        val result = target(interval)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}
