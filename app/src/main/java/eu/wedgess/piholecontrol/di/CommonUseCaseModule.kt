package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase

@Module
@InstallIn(ViewModelComponent::class)
object CommonUseCaseModule {

    @Provides
    fun provideObserveActiveUserUseCase(connectionRepository: ConnectionRepository): ObserveActiveUserUseCase =
        ObserveActiveUserUseCase(connectionRepository)

    @Provides
    fun providePeriodicRefreshUseCase(
        observeActiveUserUseCase: ObserveActiveUserUseCase,
        settingsRepository: SettingsRepository
    ): PeriodicRefreshUseCase =
        PeriodicRefreshUseCase(observeActiveUserUseCase, settingsRepository)
}
