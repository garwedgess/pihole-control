package eu.wedgess.piholecontrol.domain.usecases.statistics

import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.domain.repository.StatisticsRepository
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.statistics.view.donutchart.model.QueryTypeChartData
import kotlinx.coroutines.flow.Flow

class FetchForwardDestinationsUseCase(
    private val repository: StatisticsRepository,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<List<ForwardDestinationEntity>>> {
        return periodicRefreshUseCase { connection ->
            repository.fetchForwardDestinations(connection)
        }
    }
}