package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase

@Module
@InstallIn(ViewModelComponent::class)
object CommonUseCaseModule {

    @Provides
    fun provideObserveActiveUserUseCase(connectionRepository: ConnectionRepository): ObserveActiveUserUseCase =
        ObserveActiveUserUseCase(connectionRepository)

    @Provides
    fun providePeriodicRefreshUseCase(
        observeActiveUserUseCase: ObserveActiveUserUseCase,
        observeNetworkConnectivityUseCase: ObserveNetworkConnectivityUseCase,
        settingsRepository: SettingsRepository
    ): PeriodicRefreshUseCase =
        PeriodicRefreshUseCase(
            observeActiveUserUseCase,
            observeNetworkConnectivityUseCase,
            settingsRepository
        )
}
