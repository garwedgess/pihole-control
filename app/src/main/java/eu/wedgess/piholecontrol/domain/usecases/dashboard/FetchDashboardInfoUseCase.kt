package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.presentation.dashboard.model.DashboardInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope

class FetchDashboardInfoUseCase(
    private val fetchStatusSummaryUseCase: FetchStatusSummaryUseCase,
    private val fetchOverallTimeDataUseCase: FetchOverallTimeDataUseCase,
    private val fetchClientsOverallTimeDataUseCase: FetchClientsOverallTimeDataUseCase,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<DashboardInfo>> {
        return periodicRefreshUseCase { connection ->
            supervisorScope {
                val deferredSummary =
                    async { fetchStatusSummaryUseCase(connection) }
                val deferredOverTimeData =
                    async { fetchOverallTimeDataUseCase(connection) }
                val deferredClientsOverTimeData =
                    async { fetchClientsOverallTimeDataUseCase(connection) }

                val summaryResult = deferredSummary.await()
                val overTimeDataResult = deferredOverTimeData.await()
                val clientsOverTimeDataResult = deferredClientsOverTimeData.await()

                Result.success(
                    DashboardInfo(
                        summaryResult,
                        overTimeDataResult,
                        clientsOverTimeDataResult
                    )
                )
            }
        }
    }
}
