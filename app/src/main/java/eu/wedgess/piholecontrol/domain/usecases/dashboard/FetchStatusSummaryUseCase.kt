package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository

class FetchStatusSummaryUseCase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(connection: ConnectionInfo): Result<SummaryEntity> =
        dashboardRepository.fetchStatusSummary(connection)
}