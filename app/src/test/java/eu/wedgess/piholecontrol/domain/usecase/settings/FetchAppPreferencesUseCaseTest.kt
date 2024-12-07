package eu.wedgess.piholecontrol.domain.usecase.settings

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.AppPreferencesEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchAppPreferencesUseCaseTest {

    @MockK
    private lateinit var repository: SettingsRepository
    private lateinit var target: FetchAppPreferencesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchAppPreferencesUseCase(repository)
    }

    @Test
    fun `invoke - fetches preferences successfully`() = runTest {
        val mockPreferences: AppPreferencesEntity = mockk(relaxed = true)

        every { repository.fetchAllPreferences() } returns flowOf(mockPreferences)

        val result = target().toList()

        assertThat(result).hasSize(1)
        assertThat(result.first()).isEqualTo(mockPreferences)
    }

    @Test
    fun `invoke - emits empty preferences if repository returns nothing`() = runTest {
        every { repository.fetchAllPreferences() } returns emptyFlow()

        val result = target().toList()

        assertThat(result).isEmpty()
    }
}
