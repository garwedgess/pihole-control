package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import eu.wedgess.piholecontrol.domain.repository.LogsRepository
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogFilterSuggestionsUseCase
import eu.wedgess.piholecontrol.domain.usecases.logs.FetchLogsUseCase

@Module
@InstallIn(ViewModelComponent::class)
object LogsModule {

    @Provides
    fun provideFetchFilterRulesUseCase(
        logsRepository: LogsRepository,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchLogsUseCase = FetchLogsUseCase(
        logsRepository,
        periodicRefreshUseCase
    )

    @Provides
    fun provideFetchLogFilterSuggestionsUseCase(
        logsRepository: LogsRepository,
        observeActiveUserUseCase: ObserveActiveUserUseCase
    ): FetchLogFilterSuggestionsUseCase = FetchLogFilterSuggestionsUseCase(
        logsRepository,
        observeActiveUserUseCase
    )
}
