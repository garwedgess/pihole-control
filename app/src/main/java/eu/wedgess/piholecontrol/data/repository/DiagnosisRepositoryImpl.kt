package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.DiagnosisApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.DiagnosisMessageEntity
import eu.wedgess.piholecontrol.domain.repository.DiagnosisRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class DiagnosisRepositoryImpl(
    private val diagnosisApiService: DiagnosisApiService,
    private val dispatcherProvider: DispatcherProvider
) : DiagnosisRepository {

    override suspend fun fetchDiagnosisMessages(
        activeConnection: ConnectionEntity
    ): Result<List<DiagnosisMessageEntity>> = withContext(dispatcherProvider.io) {
        diagnosisApiService.fetchDiagnosisMessages(activeConnection)
            .mapCatching { response ->
                response.messages
                    .map { it.toEntity() }
                    .sortedByDescending { it.timestamp }
            }
    }

    override suspend fun dismissDiagnosisMessages(
        activeConnection: ConnectionEntity,
        ids: List<Int>
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        diagnosisApiService.dismissDiagnosisMessages(
            connection = activeConnection,
            ids = ids
        )
    }
}
