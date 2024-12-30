package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import kotlinx.coroutines.flow.Flow

class FetchQueryTypesUseCase(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<List<QueryTypeChartData>>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchQueryTypes(connection).mapCatching { list ->
                list.map { QueryTypeChartData(it.key, it.value) }
            }
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
