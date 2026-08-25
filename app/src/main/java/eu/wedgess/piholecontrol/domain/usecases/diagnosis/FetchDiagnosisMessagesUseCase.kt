package eu.wedgess.piholecontrol.domain.usecases.diagnosis

import eu.wedgess.piholecontrol.domain.model.DiagnosisMessageEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.DiagnosisRepository
import javax.inject.Inject

class FetchDiagnosisMessagesUseCase @Inject constructor(
    private val diagnosisRepository: DiagnosisRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(): Result<List<DiagnosisMessageEntity>> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                diagnosisRepository.fetchDiagnosisMessages(activeConnection)
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
