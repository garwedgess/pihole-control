package eu.wedgess.piholecontrol.domain.usecase.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateDynamicThemeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
    fun `invoke - successfully updates dynamic theme`() = runTest {
        coEvery { repository.updateDynamicTheme(true) } returns Result.success(Unit)
        coEvery { repository.updateDynamicTheme(false) } returns Result.success(Unit)

        val resultTrue = target(true)
        assertThat(resultTrue.isSuccess).isTrue()

        val resultFalse = target(false)
        assertThat(resultFalse.isSuccess).isTrue()

        coEvery { repository.updateDynamicTheme(true) }
        coEvery { repository.updateDynamicTheme(false) }
    }

    @Test
    fun `invoke - returns failure when repository fails to update dynamic theme`() = runTest {
        val exception = Exception("Failed to update theme")
        coEvery { repository.updateDynamicTheme(true) } returns Result.failure(exception)
        coEvery { repository.updateDynamicTheme(false) } returns Result.failure(exception)

        val resultTrue = target(true)
        assertThat(resultTrue.isFailure).isTrue()
        assertThat(resultTrue.exceptionOrNull()).isEqualTo(exception)

        val resultFalse = target(false)
        assertThat(resultFalse.isFailure).isTrue()
        assertThat(resultFalse.exceptionOrNull()).isEqualTo(exception)
    }
}
