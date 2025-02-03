package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchQueryTypesUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopClientsUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchTopQueriesUseCase
import eu.wedgess.piholecontrol.domain.usecases.statistics.FetchUpstreamDestinationsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object StatisticsModule {

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
    ): FetchUpstreamDestinationsUseCase =
        FetchUpstreamDestinationsUseCase(statisticsRepository, periodicRefreshUseCase)

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
