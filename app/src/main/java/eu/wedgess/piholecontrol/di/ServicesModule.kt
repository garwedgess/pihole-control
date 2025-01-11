package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceV5Impl
import eu.wedgess.piholecontrol.data.api.v6.AuthApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.AuthApiServiceV6Impl
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6Impl
import eu.wedgess.piholecontrol.data.api.v6.FilterRulesApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.FilterRulesApiServiceV6Impl
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6Impl
import eu.wedgess.piholecontrol.data.api.v6.StatisticsApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.StatisticsApiServiceV6Impl
import eu.wedgess.piholecontrol.data.api.v6.StatusApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.StatusApiServiceV6Impl
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
    fun provideStatusApiServiceV5(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatusApiServiceV5 = StatusApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideStatusApiServiceV6(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatusApiServiceV6 = StatusApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)

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
    fun provideFiltersApiV5(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): FilterRulesApiServiceV5 =
        FilterRulesApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideFiltersApiV6(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): FilterRulesApiServiceV6 =
        FilterRulesApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideLogsApiV5(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): LogsApiServiceV5 =
        LogsApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideLogsApiV6(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): LogsApiServiceV6 =
        LogsApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideStatisticsApiV5(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatisticsApiServiceV5 =
        StatisticsApiServiceV5Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideStatisticsApiV6(
        @DefaultHttpClient defaultHttpClient: HttpClient,
        @TrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): StatisticsApiServiceV6 =
        StatisticsApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)

    @Provides
    @Singleton
    fun provideAuthApi(
        @AuthHttpClient defaultHttpClient: HttpClient,
        @AuthTrustAllCertificatesHttpClient trustAllCertsHttpClient: HttpClient
    ): AuthApiServiceV6 =
        AuthApiServiceV6Impl(defaultHttpClient, trustAllCertsHttpClient)
}
