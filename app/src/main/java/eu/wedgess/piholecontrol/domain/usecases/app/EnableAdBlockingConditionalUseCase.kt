package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.data.model.responses.PiHoleStatusResponse
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import kotlinx.coroutines.flow.first

class EnableAdBlockingConditionalUseCase(
    private val fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase,
    private val fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
    private val observeActiveUserUseCase: ObserveActiveUserUseCase,
    private val enableAdBlockingUseCase: EnableAdBlockingUseCase
) {
    suspend operator fun invoke(): Result<StatusEntity> {
        val changeForAll = fetchShouldChangeStatusOnAllConnectionsUseCase()

        return if (changeForAll) {
            fetchAllConnectionsUseCase()
                .first()
                .fold(
                    onSuccess = { connections ->
                        connections.fold(Result.success(StatusEntity.UNKNOWN)) { acc, connection ->
                            if (acc.isFailure) {
                                acc
                            } else {
                                enableAdBlockingUseCase(connection)
                            }
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
        } else {
            observeActiveUserUseCase()
                .first()
                .fold(
                    onSuccess = { activeConnection ->
                        if (activeConnection != null) {
                            enableAdBlockingUseCase(activeConnection)
                        } else {
                            Result.failure(IllegalStateException("No active connection found"))
                        }
                    },
                    onFailure = { Result.failure(it) }
                )
        }
    }
}
