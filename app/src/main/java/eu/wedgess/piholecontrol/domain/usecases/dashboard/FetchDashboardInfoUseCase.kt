package eu.wedgess.piholecontrol.domain.usecases.dashboard

import eu.wedgess.piholecontrol.domain.model.DashboardInfoEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope

class FetchDashboardInfoUseCase(
    private val fetchStatusSummaryUseCase: FetchStatusSummaryUseCase,
    private val fetchOverallTimeDataUseCase: FetchOverallTimeDataUseCase,
    private val fetchClientsOverallTimeDataUseCase: FetchClientsOverallTimeDataUseCase,
    private val periodicRefreshUseCase: PeriodicRefreshUseCase
) {

    operator fun invoke(): Flow<Result<DashboardInfoEntity>> {
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

                if (summaryResult.isFailure && overTimeDataResult.isFailure && clientsOverTimeDataResult.isFailure) {
                    Result.failure(Throwable(summaryResult.exceptionOrNull()))
                } else {
                    Result.success(
                        DashboardInfoEntity(
                            summaryResult,
                            overTimeDataResult,
                            clientsOverTimeDataResult
                        )
                    )
                }
            }
        }
    }

    fun triggerRefresh() = periodicRefreshUseCase.triggerRefresh()
}
