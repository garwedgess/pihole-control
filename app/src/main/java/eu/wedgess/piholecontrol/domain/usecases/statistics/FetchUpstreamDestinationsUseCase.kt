package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.common.components.donutchart.model.UpstreamDestinationsChartData
import kotlinx.coroutines.flow.Flow

class FetchUpstreamDestinationsUseCase(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<List<UpstreamDestinationsChartData>>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchUpstreamDestinations(connection).mapCatching { list ->
                list.map { UpstreamDestinationsChartData(it.destination, it.percentage) }
            }
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
