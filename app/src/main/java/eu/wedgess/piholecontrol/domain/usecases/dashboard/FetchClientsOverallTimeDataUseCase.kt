package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import javax.inject.Inject

class FetchClientsOverallTimeDataUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(connection: ConnectionEntity): Result<List<ClientOverTimeEntity>> =
        dashboardRepository.fetchOverTimeDataClients(connection)
}
