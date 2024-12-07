package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.api.StatusApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.DisableAdBlockingUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingConditionalUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.EnableAdBlockingUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchAppInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchShouldChangeStatusOnAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.app.FetchStatusUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchConnectionByIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionsUseCaseModule {

    @Provides
    fun provideFetchAllConnectionsUseCase(
        repository: ConnectionRepository
    ): FetchAllConnectionsUseCase {
        return FetchAllConnectionsUseCase(repository)
    }

    @Provides
    fun provideFetchConnectionByIdUseCase(
        repository: ConnectionRepository
    ): FetchConnectionByIdUseCase {
        return FetchConnectionByIdUseCase(repository)
    }

    @Provides
    fun provideAddConnectionUseCase(
        repository: ConnectionRepository
    ): AddConnectionUseCase {
        return AddConnectionUseCase(repository)
    }

    @Provides
    fun provideUpdateConnectionUseCase(
        repository: ConnectionRepository
    ): UpdateConnectionUseCase {
        return UpdateConnectionUseCase(repository)
    }

    @Provides
    fun provideDeleteConnectionUseCase(
        repository: ConnectionRepository
    ): DeleteConnectionUseCase {
        return DeleteConnectionUseCase(repository)
    }

    @Provides
    fun provideSetConnectionAsActiveUseCase(
        repository: ConnectionRepository
    ): SetConnectionAsActiveUseCase {
        return SetConnectionAsActiveUseCase(repository)
    }
}