package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.api.DashboardApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchClientsOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchStatusSummaryUseCase
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    @Provides
    fun provideFetchStatusSummaryUseCase(dashboardRepository: DashboardRepository): FetchStatusSummaryUseCase =
        FetchStatusSummaryUseCase(dashboardRepository)

    @Provides
    fun provideFetchOverallTimeDataUseCase(dashboardRepository: DashboardRepository): FetchOverallTimeDataUseCase =
        FetchOverallTimeDataUseCase(dashboardRepository)

    @Provides
    fun provideFetchClientsOverallTimeDataUseCase(dashboardRepository: DashboardRepository): FetchClientsOverallTimeDataUseCase =
        FetchClientsOverallTimeDataUseCase(dashboardRepository)

    @Provides
    fun provideDashboardApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): DashboardApiService =
        DashboardApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)


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