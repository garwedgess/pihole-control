package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.settings.FetchAppPreferencesUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateDynamicThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateRefreshIntervalUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateSelectedThemeUseCase
import eu.wedgess.piholecontrol.domain.usecases.settings.UpdateStatusChangeOnAllConnectionsUseCase

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    fun provideUpdateSelectedThemeUseCase(
        settingsRepository: SettingsRepository
    ): UpdateSelectedThemeUseCase {
        return UpdateSelectedThemeUseCase(settingsRepository)
    }

    @Provides
    fun provideUpdateDynamicThemeUseCase(
        settingsRepository: SettingsRepository
    ): UpdateDynamicThemeUseCase {
        return UpdateDynamicThemeUseCase(settingsRepository)
    }

    @Provides
    fun provideUpdateRefreshIntervalUseCase(
        settingsRepository: SettingsRepository
    ): UpdateRefreshIntervalUseCase {
        return UpdateRefreshIntervalUseCase(settingsRepository)
    }

    @Provides
    fun provideUpdateStatusChangeOnAllConnectionsUseCase(
        settingsRepository: SettingsRepository
    ): UpdateStatusChangeOnAllConnectionsUseCase {
        return UpdateStatusChangeOnAllConnectionsUseCase(settingsRepository)
    }

    @Provides
    fun provideFetchAppPreferencesUseCase(
        settingsRepository: SettingsRepository
    ): FetchAppPreferencesUseCase {
        return FetchAppPreferencesUseCase(settingsRepository)
    }
}