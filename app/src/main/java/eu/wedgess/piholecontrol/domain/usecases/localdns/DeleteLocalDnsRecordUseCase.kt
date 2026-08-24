package eu.wedgess.piholecontrol.domain.usecases.localdns

import eu.wedgess.piholecontrol.domain.model.ModifyLocalDnsRecordResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import javax.inject.Inject

class DeleteLocalDnsRecordUseCase @Inject constructor(
    private val localDnsRepository: LocalDnsRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        value: String
    ): Result<ModifyLocalDnsRecordResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                localDnsRepository.deleteLocalDnsRecord(
                    activeConnection = activeConnection,
                    value = value
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
