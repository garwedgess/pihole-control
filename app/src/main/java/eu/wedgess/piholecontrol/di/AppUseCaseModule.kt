package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.CheckHasConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchAppInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchShouldChangeStatusOnAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchStatusUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object AppUseCaseModule {

    @Provides
    fun provideFetchStatusUseCase(
        repository: StatusRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchStatusUseCase {
        return FetchStatusUseCase(repository, periodicRefreshUseCase)
    }

    @Provides
    fun provideFetchAppInfoUseCase(
        fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
        observeActiveUserUseCase: ObserveActiveUserUseCase,
        fetchStatusUseCase: FetchStatusUseCase
    ): FetchAppInfoUseCase {
        return FetchAppInfoUseCase(
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            fetchStatusUseCase
        )
    }

    @Provides
    fun provideFetchShouldChangeStatusOnAllConnectionsUseCase(
        settingsRepository: SettingsRepository
    ): FetchShouldChangeStatusOnAllConnectionsUseCase {
        return FetchShouldChangeStatusOnAllConnectionsUseCase(settingsRepository)
    }

    @Provides
    fun provideEnableAdBlockingUseCase(
        repository: StatusRepository
    ): EnableAdBlockingUseCase {
        return EnableAdBlockingUseCase(repository)
    }

    @Provides
    fun provideEnableAdBlockingConditionalUseCase(
        fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase,
        fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
        observeActiveUserUseCase: ObserveActiveUserUseCase,
        enableAdBlockingUseCase: EnableAdBlockingUseCase
    ): EnableAdBlockingConditionalUseCase {
        return EnableAdBlockingConditionalUseCase(
            fetchShouldChangeStatusOnAllConnectionsUseCase,
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            enableAdBlockingUseCase
        )
    }

    @Provides
    fun provideDisableAdBlockingUseCase(
        repository: StatusRepository
    ): DisableAdBlockingUseCase {
        return DisableAdBlockingUseCase(repository)
    }

    @Provides
    fun provideDisableAdBlockingConditionalUseCase(
        fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase,
        fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
        observeActiveUserUseCase: ObserveActiveUserUseCase,
        disableAdBlockingUseCase: DisableAdBlockingUseCase
    ): DisableAdBlockingConditionalUseCase {
        return DisableAdBlockingConditionalUseCase(
            fetchShouldChangeStatusOnAllConnectionsUseCase,
            fetchAllConnectionsUseCase,
            observeActiveUserUseCase,
            disableAdBlockingUseCase
        )
    }

    @Provides
    fun provideCheckHasConnectionsUseCase(
        repository: ConnectionRepository
    ): CheckHasConnectionsUseCase {
        return CheckHasConnectionsUseCase(repository)
    }
}
