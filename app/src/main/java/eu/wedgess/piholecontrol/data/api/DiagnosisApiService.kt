package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleDiagnosisMessagesResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface DiagnosisApiService {

    suspend fun fetchDiagnosisMessages(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleDiagnosisMessagesResponseData>

    suspend fun dismissDiagnosisMessages(
        connection: ConnectionEntity,
        ids: List<Int>
    ): PiHoleApiResult<Unit>
}
