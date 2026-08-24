package eu.wedgess.piholecontrol.domain.usecases.localdns

import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordUpdateEntity
import eu.wedgess.piholecontrol.domain.model.ModifyLocalDnsRecordResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import javax.inject.Inject

class UpdateLocalDnsRecordUseCase @Inject constructor(
    private val localDnsRepository: LocalDnsRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        update: LocalDnsRecordUpdateEntity
    ): Result<ModifyLocalDnsRecordResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                localDnsRepository.updateLocalDnsRecord(
                    activeConnection = activeConnection,
                    update = update
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
