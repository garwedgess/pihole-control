package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchClientsOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchStatusSummaryUseCase

@Module
@InstallIn(ViewModelComponent::class)
object DashboardModule {

    @Provides
    fun provideFetchStatusSummaryUseCase(
        dashboardRepository: DashboardRepository
    ): FetchStatusSummaryUseCase =
        FetchStatusSummaryUseCase(dashboardRepository)

    @Provides
    fun provideFetchOverallTimeDataUseCase(
        dashboardRepository: DashboardRepository
    ): FetchOverallTimeDataUseCase =
        FetchOverallTimeDataUseCase(dashboardRepository)

    @Provides
    fun provideFetchClientsOverallTimeDataUseCase(
        dashboardRepository: DashboardRepository
    ): FetchClientsOverallTimeDataUseCase =
        FetchClientsOverallTimeDataUseCase(dashboardRepository)

    @Provides
    fun provideFetchDashboardInfoUseCase(
        fetchStatusSummaryUseCase: FetchStatusSummaryUseCase,
        fetchOverallTimeDataUseCase: FetchOverallTimeDataUseCase,
        fetchClientsOverallTimeDataUseCase: FetchClientsOverallTimeDataUseCase,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchDashboardInfoUseCase = FetchDashboardInfoUseCase(
        fetchStatusSummaryUseCase,
        fetchOverallTimeDataUseCase,
        fetchClientsOverallTimeDataUseCase,
        periodicRefreshUseCase
    )
}
