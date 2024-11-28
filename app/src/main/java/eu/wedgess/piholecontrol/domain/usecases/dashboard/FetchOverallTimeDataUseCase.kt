package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeData
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository

class FetchOverallTimeDataUseCase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(connection: ConnectionInfo): Result<QueriesOverTimeEntity> =
        dashboardRepository.fetchOverTimeData10Minutes(connection)
}