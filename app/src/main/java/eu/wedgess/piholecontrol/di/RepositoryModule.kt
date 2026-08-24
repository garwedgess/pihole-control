package eu.wedgess.piholecontrol.di

import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.AuthApiService
import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.api.GroupApiService
import eu.wedgess.piholecontrol.data.api.LocalDnsApiService
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.repository.AuthRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.ConnectionRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.DashboardRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.FilterRulesRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.GroupRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.LocalDnsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.LogsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.SettingsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.StatisticsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.StatusRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.TokenRefresher
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.domain.repository.GroupRepository
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDashboardRepository(
        apiV6: DashboardApiService,
        dispatcherProvider: DispatcherProvider
    ): DashboardRepository =
        DashboardRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        dataStore: DataStore<UserPreferences>,
        dispatcherProvider: DispatcherProvider
    ): SettingsRepository =
        SettingsRepositoryImpl(dataStore, dispatcherProvider)

    @Provides
    @Singleton
    fun provideConnectionRepository(
        connectionVersion6Dao: ConnectionDao,
        dispatcherProvider: DispatcherProvider
    ): ConnectionRepository =
        ConnectionRepositoryImpl(
            connectionVersion6Dao,
            dispatcherProvider
        )

    @Provides
    @Singleton
    fun provideFiltersRepository(
        apiV6: FilterRulesApiService,
        dispatcherProvider: DispatcherProvider
    ): FilterRulesRepository =
        FilterRulesRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideLocalDnsRepository(
        apiV6: LocalDnsApiService,
        dispatcherProvider: DispatcherProvider
    ): LocalDnsRepository =
        LocalDnsRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideLogsRepository(
        apiV6: LogsApiService,
        dispatcherProvider: DispatcherProvider
    ): LogsRepository =
        LogsRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatusRepository(
        apiV6: StatusApiService,
        dispatcherProvider: DispatcherProvider
    ): StatusRepository =
        StatusRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatisticsRepository(
        apiV6: StatisticsApiService,
        dispatcherProvider: DispatcherProvider
    ): StatisticsRepository =
        StatisticsRepositoryImpl(apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApiService,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository =
        AuthRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideGroupsRepository(
        api: GroupApiService,
        dispatcherProvider: DispatcherProvider
    ): GroupRepository =
        GroupRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideTokenRefresher(
        api: AuthApiService,
        dispatcherProvider: DispatcherProvider
    ): TokenRefresher =
        AuthRepositoryImpl(api, dispatcherProvider)
}
