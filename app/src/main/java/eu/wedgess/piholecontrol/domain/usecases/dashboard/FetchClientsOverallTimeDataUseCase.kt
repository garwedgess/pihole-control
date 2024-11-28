package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository

class FetchClientsOverallTimeDataUseCase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(connection: ConnectionInfo): Result<List<ClientOverTimeEntity>> =
        dashboardRepository.fetchOverTimeDataClients(connection)
}