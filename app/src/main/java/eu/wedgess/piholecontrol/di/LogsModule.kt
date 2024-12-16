package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.api.LogsApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
object LogsModule {

    @Provides
    fun provideLogsApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): LogsApiService =
        LogsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    fun provideFetchFilterRulesUseCase(
        logsRepository: LogsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchLogsUseCase = FetchLogsUseCase(
        logsRepository,
        periodicRefreshUseCase
    )
}
