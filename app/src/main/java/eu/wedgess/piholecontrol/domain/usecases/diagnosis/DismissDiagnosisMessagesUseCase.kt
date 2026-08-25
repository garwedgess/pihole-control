package eu.wedgess.piholecontrol.domain.usecases.diagnosis

import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.DiagnosisRepository
import javax.inject.Inject

class DismissDiagnosisMessagesUseCase @Inject constructor(
    private val diagnosisRepository: DiagnosisRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(ids: List<Int>): Result<Unit> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                diagnosisRepository.dismissDiagnosisMessages(activeConnection, ids)
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
