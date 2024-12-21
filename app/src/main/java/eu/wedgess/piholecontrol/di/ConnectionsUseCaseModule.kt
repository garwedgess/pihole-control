package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.usecases.connections.AddConnectionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.DeleteConnectionMarkedForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchConnectionByIdUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.MarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.SetConnectionAsActiveUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UnMarkConnectionForDeletionUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.UpdateConnectionUseCase

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
    ): DeleteConnectionMarkedForDeletionUseCase {
        return DeleteConnectionMarkedForDeletionUseCase(repository)
    }

    @Provides
    fun provideSetConnectionAsActiveUseCase(
        repository: ConnectionRepository
    ): SetConnectionAsActiveUseCase {
        return SetConnectionAsActiveUseCase(repository)
    }

    @Provides
    fun provideMarkConnectionForDeletionUseCase(
        repository: ConnectionRepository
    ): MarkConnectionForDeletionUseCase {
        return MarkConnectionForDeletionUseCase(repository)
    }

    @Provides
    fun provideUnMarkConnectionForDeletionUseCase(
        repository: ConnectionRepository
    ): UnMarkConnectionForDeletionUseCase {
        return UnMarkConnectionForDeletionUseCase(repository)
    }
}
