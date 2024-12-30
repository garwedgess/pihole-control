package eu.wedgess.piholecontrol.domain.usecase.app

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.app.FetchShouldChangeStatusOnAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchShouldChangePiHoleSummaryStatusV6DataOnAllConnectionsUseCaseTest {

    @MockK
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchShouldChangeStatusOnAllConnectionsUseCase =
            FetchShouldChangeStatusOnAllConnectionsUseCase(
                settingsRepository
            )
    }

    @Test
    fun `invoke - returns true when settingsRepository returns true`() = runTest {
        coEvery { settingsRepository.changeStatusOnAllConnection() } returns flowOf(true)

        val result = fetchShouldChangeStatusOnAllConnectionsUseCase()

        assertThat(result).isTrue()
        coVerify { settingsRepository.changeStatusOnAllConnection() }
    }

    @Test
    fun `invoke - returns false when settingsRepository returns false`() = runTest {
        coEvery { settingsRepository.changeStatusOnAllConnection() } returns flowOf(false)

        val result = fetchShouldChangeStatusOnAllConnectionsUseCase()

        assertThat(result).isFalse()
        coVerify { settingsRepository.changeStatusOnAllConnection() }
    }
}
