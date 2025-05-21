package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import javax.inject.Inject

class FetchOverallTimeDataUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(connection: ConnectionEntity): Result<QueriesOverTimeEntity> =
        dashboardRepository.fetchOverTimeData10Minutes(connection)
}
