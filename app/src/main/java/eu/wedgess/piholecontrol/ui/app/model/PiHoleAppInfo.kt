package eu.wedgess.piholecontrol.ui.app.model

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatus

data class PiHoleAppInfo(
    val currentConnection: ConnectionInfo,
    val connections: List<ConnectionInfo>,
    val status: PiHoleStatus,
    val refreshInterval: Long
) {
    companion object {
        fun initial() = PiHoleAppInfo(
            currentConnection = ConnectionInfo.default,
            connections = emptyList(),
            refreshInterval = 10_000,
            status = PiHoleStatus.UNKNOWN
        )
    }
}
