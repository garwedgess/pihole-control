package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import androidx.datastore.core.DataStore
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    @MockK
    private lateinit var preferences: DataStore<UserPreferences>
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: SettingsRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = SettingsRepositoryImpl(preferences, dispatcherProvider)
    }

    @Test
    fun `fetchAllPreferences - preferences data is mapped correctly`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        target.fetchAllPreferences().test {
            val result = awaitItem()
            assertThat(result.theme).isEqualTo(AppThemeEntity.SYSTEM)
            assertThat(result.useDynamicColors).isFalse()
            assertThat(result.refreshInterval).isEqualTo(30L)
            assertThat(result.multiStatusChange).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getRefreshInterval - returns the correct refresh interval`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        target.getRefreshInterval().test {
            val interval = awaitItem()
            assertThat(interval).isEqualTo(30L)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `changeStatusOnAllConnection - returns the correct status`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        target.changeStatusOnAllConnection().test {
            val status = awaitItem()
            assertThat(status).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateSelectedTheme - updates the theme successfully`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        coEvery { preferences.updateData(any()) } answers {
            userPreferencesFlow.value = userPreferencesFlow.value.toBuilder()
                .setTheme(UserPreferences.Theme.DARK)
                .build()
            userPreferencesFlow.value
        }

        val result = target.updateSelectedTheme(AppThemeEntity.DARK)

        assertThat(result.isSuccess).isTrue()
        coVerify { preferences.updateData(any()) }
    }

    @Test
    fun `updateSelectedTheme - handles failure`() = runTest {
        coEvery { preferences.updateData(any()) } throws RuntimeException("Update error")

        val result = target.updateSelectedTheme(AppThemeEntity.DARK)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        coVerify { preferences.updateData(any()) }
    }

    @Test
    fun `updateDynamicTheme - updates the dynamic theme preference`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        coEvery { preferences.updateData(any()) } answers {
            userPreferencesFlow.value = userPreferencesFlow.value.toBuilder()
                .setUseDynamicColors(true)
                .build()
            userPreferencesFlow.value
        }

        val result = target.updateDynamicTheme(true)

        assertThat(result.isSuccess).isTrue()
        coVerify { preferences.updateData(any()) }
    }

    @Test
    fun `updateRefreshInterval - updates the refresh interval`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        coEvery { preferences.updateData(any()) } answers {
            userPreferencesFlow.value = userPreferencesFlow.value.toBuilder()
                .setRefreshTime(60L)
                .build()
            userPreferencesFlow.value
        }

        val result = target.updateRefreshInterval(60L)

        assertThat(result.isSuccess).isTrue()
        coVerify { preferences.updateData(any()) }
    }

    @Test
    fun `updateStatusChangeOnAllConnections - updates the status change preference`() = runTest {
        val userPreferencesFlow = MutableStateFlow(
            UserPreferences.newBuilder()
                .setTheme(UserPreferences.Theme.SYSTEM)
                .setUseDynamicColors(false)
                .setRefreshTime(30L)
                .setChangeStatusOnAllConnection(false)
                .build()
        )
        coEvery { preferences.data } returns userPreferencesFlow
        coEvery { preferences.updateData(any()) } answers {
            userPreferencesFlow.value = userPreferencesFlow.value.toBuilder()
                .setChangeStatusOnAllConnection(true)
                .build()
            userPreferencesFlow.value
        }

        val result = target.updateStatusChangeOnAllConnections(true)

        assertThat(result.isSuccess).isTrue()
        coVerify { preferences.updateData(any()) }
    }
}
