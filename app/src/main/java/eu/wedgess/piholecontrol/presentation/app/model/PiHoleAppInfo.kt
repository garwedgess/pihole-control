package eu.wedgess.piholecontrol.presentation.app.model

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus
import eu.wedgess.piholecontrol.domain.model.StatusEntity

data class PiHoleAppInfo(
    val currentConnection: ConnectionInfo,
    val connections: List<ConnectionInfo>,
    val status: StatusEntity
) {
    companion object {
        fun initial() = PiHoleAppInfo(
            currentConnection = ConnectionInfo.default,
            connections = emptyList(),
            status = StatusEntity.UNKNOWN
        )
    }
}
