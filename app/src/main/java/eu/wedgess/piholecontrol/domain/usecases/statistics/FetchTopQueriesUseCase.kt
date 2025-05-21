package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchTopQueriesUseCase @Inject constructor(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<TopQueriesEntity>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchTopQueries(connection)
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
