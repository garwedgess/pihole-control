package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.AuthApiService
import eu.wedgess.piholecontrol.data.api.AuthApiServiceImpl
import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.api.DashboardApiServiceImpl
import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.api.FilterRulesApiServiceImpl
import eu.wedgess.piholecontrol.data.api.GroupApiService
import eu.wedgess.piholecontrol.data.api.GroupApiServiceImpl
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.api.LogsApiServiceImpl
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.api.StatisticsApiServiceImpl
import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.api.StatusApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.AuthHttpClient
import eu.wedgess.piholecontrol.di.annotations.AuthTrustAllCertificatesHttpClient
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
    fun provideDashboardApi(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): DashboardApiService =
        DashboardApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

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

    @Provides
    @Singleton
    fun provideAuthApi(
        @AuthHttpClient defaultHttpClient: HttpClient,
        @AuthTrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): AuthApiService =
        AuthApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideGroupApi(
        @AuthHttpClient defaultHttpClient: HttpClient,
        @AuthTrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): GroupApiService =
        GroupApiServiceImpl(defaultHttpClient, trustAllCertsHttpClient)
}
