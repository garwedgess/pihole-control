package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.DiagnosisMessageEntity

interface DiagnosisRepository {

    suspend fun fetchDiagnosisMessages(
        activeConnection: ConnectionEntity
    ): Result<List<DiagnosisMessageEntity>>

    suspend fun dismissDiagnosisMessages(
        activeConnection: ConnectionEntity,
        ids: List<Int>
    ): Result<Unit>
}
