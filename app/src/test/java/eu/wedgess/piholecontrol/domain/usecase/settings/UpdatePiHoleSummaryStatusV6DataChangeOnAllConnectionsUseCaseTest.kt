package eu.wedgess.piholecontrol.domain.usecase.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateStatusChangeOnAllConnectionsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdatePiHoleSummaryStatusV6DataChangeOnAllConnectionsUseCaseTest {

    @MockK
    private lateinit var repository: SettingsRepository

    private lateinit var target: UpdateStatusChangeOnAllConnectionsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = UpdateStatusChangeOnAllConnectionsUseCase(repository)
    }

    @Test
    fun `invoke - successfully updates status change on all connections`() = runTest {
        val applyOnAll = true
        coEvery { repository.updateStatusChangeOnAllConnections(applyOnAll) } returns Result.success(
            Unit
        )

        val result = target(applyOnAll)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.updateStatusChangeOnAllConnections(applyOnAll) }
    }

    @Test
    fun `invoke - returns failure when repository fails to update status change on all connections`() =
        runTest {
            val applyOnAll = true
            val exception = Exception("Failed to update status on all connections")
            coEvery { repository.updateStatusChangeOnAllConnections(applyOnAll) } returns Result.failure(
                exception
            )

            val result = target(applyOnAll)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { repository.updateStatusChangeOnAllConnections(applyOnAll) }
        }
}
