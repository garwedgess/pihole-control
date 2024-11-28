package eu.wedgess.piholecontrol.di

import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.ConnectionRepositoryImpl
import eu.wedgess.piholecontrol.data.DashboardRepositoryImpl
import eu.wedgess.piholecontrol.data.FilterRulesRepositoryImpl
import eu.wedgess.piholecontrol.data.LogsRepositoryImpl
import eu.wedgess.piholecontrol.data.SettingsRepositoryImpl
import eu.wedgess.piholecontrol.data.StatisticsRepositoryImpl
import eu.wedgess.piholecontrol.data.StatusRepositoryImpl
import eu.wedgess.piholecontrol.data.api.DashboardApiService
import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.api.LogsApiService
import eu.wedgess.piholecontrol.data.api.LogsApiServiceImpl
import eu.wedgess.piholecontrol.data.api.StatisticsApiService
import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.repository.SettingsRepository
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchClientsOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchDashboardInfoUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchOverallTimeDataUseCase
import eu.wedgess.piholecontrol.domain.usecases.dashboard.FetchStatusSummaryUseCase
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDashboardRepository(
        api: DashboardApiService,
        dispatcherProvider: DispatcherProvider
    ): DashboardRepository =
        DashboardRepositoryImpl(api, dispatcherProvider)

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
        connectionDao: ConnectionDao,
        dispatcherProvider: DispatcherProvider
    ): ConnectionRepository =
        ConnectionRepositoryImpl(connectionDao, dispatcherProvider)

    @Provides
    @Singleton
    fun provideFiltersRepository(
        api: FilterRulesApiService,
        dispatcherProvider: DispatcherProvider
    ): FilterRulesRepository =
        FilterRulesRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideLogsRepository(
        api: LogsApiService,
        dispatcherProvider: DispatcherProvider
    ): LogsRepository =
        LogsRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatusRepository(
        api: StatusApiService,
        dispatcherProvider: DispatcherProvider
    ): StatusRepository =
        StatusRepositoryImpl(api, dispatcherProvider)

    @Provides
    @Singleton
    fun provideStatisticsRepository(
        api: StatisticsApiService,
        dispatcherProvider: DispatcherProvider
    ): StatisticsRepository =
        StatisticsRepositoryImpl(api, dispatcherProvider)

}