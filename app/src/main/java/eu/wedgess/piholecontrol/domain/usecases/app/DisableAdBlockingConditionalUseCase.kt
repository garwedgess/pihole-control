package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import kotlinx.coroutines.flow.first
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DisableAdBlockingConditionalUseCase(
    private val fetchShouldChangeStatusOnAllConnectionsUseCase: FetchShouldChangeStatusOnAllConnectionsUseCase,
    private val fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
    private val observeActiveUserUseCase: ObserveActiveUserUseCase,
    private val disableAdBlockingUseCase: DisableAdBlockingUseCase
) {
    suspend operator fun invoke(duration: Long): Result<StatusEntity> {
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
                                disableAdBlockingUseCase(
                                    connection,
                                    duration.toDuration(DurationUnit.MILLISECONDS)
                                )
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
                        disableAdBlockingUseCase(
                            activeConnection,
                            duration.toDuration(DurationUnit.MILLISECONDS)
                        )
                    },
                    onFailure = { Result.failure(it) }
                )
        }
    }
}
