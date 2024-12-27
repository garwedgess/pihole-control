package eu.wedgess.piholecontrol.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.network.NetworkConnectivityObserverImpl
import eu.wedgess.piholecontrol.domain.network.NetworkConnectivityObserver
import eu.wedgess.piholecontrol.domain.usecases.app.ObserveNetworkConnectivityUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkObserverModule {

    @Binds
    @Singleton
    abstract fun bindNetworkConnectivityObserver(
        networkConnectivityObserverImpl: NetworkConnectivityObserverImpl
    ): NetworkConnectivityObserver

    companion object {
        @Provides
        @Singleton
        fun provideObserveNetworkConnectivityUseCase(
            networkConnectivityObserver: NetworkConnectivityObserver
        ): ObserveNetworkConnectivityUseCase {
            return ObserveNetworkConnectivityUseCase(networkConnectivityObserver)
        }
    }
}
