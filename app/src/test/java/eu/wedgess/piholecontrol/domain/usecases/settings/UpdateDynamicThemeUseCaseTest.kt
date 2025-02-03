package eu.wedgess.piholecontrol.domain.usecases.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateDynamicThemeUseCaseTest {

    @MockK
    private lateinit var repository: SettingsRepository
    private lateinit var target: UpdateDynamicThemeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = UpdateDynamicThemeUseCase(repository)
    }

    @Test
    fun `invoke - successfully updates dynamic theme to true`() = runTest {
        coEvery { repository.updateDynamicTheme(true) } returns Result.success(Unit)

        val result = target(true)
        assertThat(result.isSuccess).isTrue()
        coVerify { repository.updateDynamicTheme(true) }
    }

    @Test
    fun `invoke - successfully updates dynamic theme to false`() = runTest {
        coEvery { repository.updateDynamicTheme(false) } returns Result.success(Unit)

        val result = target(false)
        assertThat(result.isSuccess).isTrue()
        coVerify { repository.updateDynamicTheme(false) }
    }

    @Test
    fun `invoke - returns failure when repository fails to update dynamic theme to true`() =
        runTest {
            val exception = Exception("Failed to update theme")
            coEvery { repository.updateDynamicTheme(true) } returns Result.failure(exception)

            val result = target(true)
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { repository.updateDynamicTheme(true) }
        }

    @Test
    fun `invoke - returns failure when repository fails to update dynamic theme to false`() =
        runTest {
            val exception = Exception("Failed to update theme")
            coEvery { repository.updateDynamicTheme(false) } returns Result.failure(exception)

            val result = target(false)
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify { repository.updateDynamicTheme(false) }
        }
}
