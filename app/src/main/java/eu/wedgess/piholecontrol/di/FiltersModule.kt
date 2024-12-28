package eu.wedgess.piholecontrol.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.api.FilterRulesApiServiceImpl
import eu.wedgess.piholecontrol.di.annotations.DefaultHttpClient
import eu.wedgess.piholecontrol.di.annotations.TrustAllCertificatesHttpClient
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.AddFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.RemoveFilterRuleUseCase
import io.ktor.client.HttpClient

@Module
@InstallIn(ViewModelComponent::class)
object FiltersModule {

    @Provides
    fun provideAddFilterRuleUseCase(
        filterRulesRepository: FilterRulesRepository,
        connectionRepository: ConnectionRepository
    ): AddFilterRuleUseCase =
        AddFilterRuleUseCase(filterRulesRepository, connectionRepository)

    @Provides
    fun provideRemoveFilterRuleUseCase(
        filterRulesRepository: FilterRulesRepository,
        connectionRepository: ConnectionRepository
    ): RemoveFilterRuleUseCase =
        RemoveFilterRuleUseCase(filterRulesRepository, connectionRepository)

    @Provides
    fun provideFetchFilterRuleUseCase(
        filterRulesRepository: FilterRulesRepository
    ): FetchFilterRuleUseCase =
        FetchFilterRuleUseCase(filterRulesRepository)

    @Provides
    fun provideFetchFilterRulesUseCase(
        fetchFilterRuleUseCase: FetchFilterRuleUseCase,
        periodicRefreshUseCase: PeriodicRefreshUseCase
    ): FetchFilterRulesUseCase = FetchFilterRulesUseCase(
        fetchFilterRuleUseCase,
        periodicRefreshUseCase
    )
}
