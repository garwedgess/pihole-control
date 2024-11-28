package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.api.StatisticsApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchForwardDestinationsUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchQueryTypesUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopClientsUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopQueriesUseCase
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    @Provides
    fun provideLogsApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatisticsApiService =
        StatisticsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    fun provideFetchQueryTypesUseCase(
        statisticsRepository: StatisticsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchQueryTypesUseCase =
        FetchQueryTypesUseCase(statisticsRepository, periodicRefreshUseCase)

    @Provides
    fun provideFetchForwardDestinationsUseCase(
        statisticsRepository: StatisticsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchForwardDestinationsUseCase =
        FetchForwardDestinationsUseCase(statisticsRepository, periodicRefreshUseCase)

    @Provides
    fun provideFetchTopQueriesUseCase(
        statisticsRepository: StatisticsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchTopQueriesUseCase =
        FetchTopQueriesUseCase(statisticsRepository, periodicRefreshUseCase)

    @Provides
    fun provideFetchTopClientsUseCase(
        statisticsRepository: StatisticsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchTopClientsUseCase =
        FetchTopClientsUseCase(statisticsRepository, periodicRefreshUseCase)

}