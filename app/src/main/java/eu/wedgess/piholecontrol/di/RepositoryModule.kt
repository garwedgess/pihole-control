package eu.wedgess.piholecontrol.di

import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.v5.DashboardApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.LogsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.StatisticsApiServiceV5
import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.AuthApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.DashboardApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.FilterRulesApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.LogsApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.StatisticsApiServiceV6
import eu.wedgess.piholecontrol.data.api.v6.StatusApiServiceV6
import eu.wedgess.piholecontrol.data.db.ConnectionVersion5Dao
import eu.wedgess.piholecontrol.data.db.ConnectionVersion6Dao
import eu.wedgess.piholecontrol.data.db.ConnectionViewDao
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.repository.AuthRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.ConnectionRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.DashboardRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.FilterRulesRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.LogsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.SettingsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.StatisticsRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.StatusRepositoryImpl
import eu.wedgess.piholecontrol.data.repository.TokenRefresher
import eu.wedgess.piholecontrol.domain.repository.AuthRepository
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
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
        apiV5: DashboardApiServiceV5,
        apiV6: DashboardApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): DashboardRepository =
        DashboardRepositoryImpl(apiV5, apiV6, dispatcherProvider)

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
        connectionVersion5Dao: ConnectionVersion5Dao,
        connectionVersion6Dao: ConnectionVersion6Dao,
        connectionViewDao: ConnectionViewDao,
        dispatcherProvider: DispatcherProvider
    ): ConnectionRepository =
        ConnectionRepositoryImpl(
            connectionVersion5Dao,
            connectionVersion6Dao,
            connectionViewDao,
            dispatcherProvider
        )

    @Provides
    @Singleton
    fun provideFiltersRepository(
        apiV5: FilterRulesApiServiceV5,
        apiV6: FilterRulesApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): FilterRulesRepository =
        FilterRulesRepositoryImpl(apiV5, apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideLogsRepository(
        apiV5: LogsApiServiceV5,
        apiV6: LogsApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): LogsRepository =
        LogsRepositoryImpl(apiV5, apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatusRepository(
        apiV5: StatusApiServiceV5,
        apiV6: StatusApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): StatusRepository =
        StatusRepositoryImpl(apiV5, apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatisticsRepository(
        apiV5: StatisticsApiServiceV5,
        apiV6: StatisticsApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): StatisticsRepository =
        StatisticsRepositoryImpl(apiV5, apiV6, dispatcherProvider)

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): AuthRepository =
        AuthRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideTokenRefresher(
        api: AuthApiServiceV6,
        dispatcherProvider: DispatcherProvider
    ): TokenRefresher =
        AuthRepositoryImpl(api, dispatcherProvider)
}
