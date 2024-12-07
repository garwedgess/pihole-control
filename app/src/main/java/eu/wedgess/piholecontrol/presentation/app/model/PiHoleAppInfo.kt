package eu.wedgess.piholecontrol.presentation.app.model

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity

data class PiHoleAppInfo(
    val currentConnection: ConnectionEntity,
    val connections: List<ConnectionEntity>,
    val status: StatusEntity
) {
    companion object {
        fun initial() = PiHoleAppInfo(
            currentConnection = ConnectionEntity.default,
            connections = emptyList(),
            status = StatusEntity.UNKNOWN
        )
    }
}
