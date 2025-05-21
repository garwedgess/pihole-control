package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.domain.repository.DashboardRepository
import javax.inject.Inject

class FetchStatusSummaryUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    suspend operator fun invoke(connection: ConnectionEntity): Result<SummaryEntity> =
        dashboardRepository.fetchStatusSummary(connection)
}
