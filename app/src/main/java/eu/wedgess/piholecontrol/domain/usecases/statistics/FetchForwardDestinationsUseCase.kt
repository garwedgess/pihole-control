package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.ForwardDestinationsChartData
import kotlinx.coroutines.flow.Flow

class FetchForwardDestinationsUseCase(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<List<ForwardDestinationsChartData>>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchForwardDestinations(connection).mapCatching { list ->
                list.map { ForwardDestinationsChartData(it.destination, it.percentage) }
            }
        }
    }

    fun refresh() = periodicRefreshUseCase.triggerRefresh()
}
