package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiService
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceImpl
import eu.wedgess.piholecontrol.data.api.v5.LogsApiService
import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceImpl
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiService
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceImpl
import eu.wedgess.piholecontrol.data.api.v5.StatusApiService
import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceImpl
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6Impl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Provides
    @Singleton
    fun provideStatusApiService(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatusApiService = StatusApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideDashboardApiV5(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): DashboardApiServiceV5 =
        DashboardApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideDashboardApiV6(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): DashboardApiServiceV6 =
        DashboardApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideFiltersApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): FilterRulesApiService =
        FilterRulesApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideLogsApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): LogsApiService =
        LogsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideStatisticsApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatisticsApiService =
        StatisticsApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
}
