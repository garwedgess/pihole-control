package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.QueryTypeChartData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchQueryTypesUseCase @Inject constructor(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<List<QueryTypeChartData>>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchQueryTypes(connection).mapCatching { list ->
                list.map { QueryTypeChartData(it.type, it.percentage) }
            }
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
