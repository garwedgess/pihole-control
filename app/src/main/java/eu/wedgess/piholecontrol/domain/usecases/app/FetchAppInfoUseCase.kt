package eu.wedgess.piholecontrol.domain.usecases.app

import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.usecases.ObserveActiveUserUseCase
import eu.wedgess.piholecontrol.domain.usecases.connections.FetchAllConnectionsUseCase
import eu.wedgess.piholecontrol.presentation.app.model.PiHoleAppInfo
import eu.wedgess.piholecontrol.presentation.base.RefreshFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FetchAppInfoUseCase(
    private val fetchAllConnectionsUseCase: FetchAllConnectionsUseCase,
    private val observeActiveUserUseCase: ObserveActiveUserUseCase,
    private val fetchStatusUseCase: FetchStatusUseCase
) {

    private val refreshFlow = RefreshFlow()

    operator fun invoke(): Flow<PiHoleAppInfo> =
        refreshFlow.flatMapLatest {
            combine(
                fetchAllConnectionsUseCase(),
                observeActiveUserUseCase(),
                fetchStatusUseCase()
            ) { connections, activeConnection, status ->
                PiHoleAppInfo(
                    connections = connections.getOrDefault(emptyList()),
                    currentConnection = activeConnection.getOrThrow(),
                    status = status.getOrNull() ?: StatusEntity.UNKNOWN
                )
            }
        }

    fun refresh() = refreshFlow.refresh()
}