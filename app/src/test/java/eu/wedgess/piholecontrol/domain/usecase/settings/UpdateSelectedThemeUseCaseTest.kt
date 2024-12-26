package eu.wedgess.piholecontrol.domain.usecase.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateSelectedThemeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateSelectedThemeUseCaseTest {

    @MockK
    private lateinit var repository: SettingsRepository

    private lateinit var target: UpdateSelectedThemeUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = UpdateSelectedThemeUseCase(repository)
    }

    @Test
    fun `invoke - successfully updates selected theme`() = runTest {
        val theme = AppThemeEntity.DARK
        coEvery { repository.updateSelectedTheme(theme) } returns Result.success(Unit)

        val result = target(theme)

        assertThat(result.isSuccess).isTrue()
        coVerify { repository.updateSelectedTheme(theme) }
    }

    @Test
    fun `invoke - returns failure when repository fails to update selected theme`() = runTest {
        val theme = AppThemeEntity.DARK
        val exception = Exception("Failed to update theme")

        coEvery { repository.updateSelectedTheme(theme) } returns Result.failure(exception)

        val result = target(theme)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { repository.updateSelectedTheme(theme) }
    }
}
