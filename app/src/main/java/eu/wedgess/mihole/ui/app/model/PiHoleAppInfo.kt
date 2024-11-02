package eu.wedgess.mihole.ui.app.model

import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.enums.PiHoleStatus

data class PiHoleAppInfo(
    val currentConnection: PiHoleInfo,
    val connections: List<PiHoleInfo>,
    val status: PiHoleStatus,
    val refreshInterval: Long
) {
    companion object {
        fun initial() = PiHoleAppInfo(
            currentConnection = PiHoleInfo.default,
            connections = emptyList(),
            refreshInterval = 10_000,
            status = PiHoleStatus.UNKNOWN
        )
    }
}
