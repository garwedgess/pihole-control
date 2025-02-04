package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.flow.Flow

class FetchTopClientsUseCase(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<TopClientQueriesEntity>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchTopClients(connection)
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
