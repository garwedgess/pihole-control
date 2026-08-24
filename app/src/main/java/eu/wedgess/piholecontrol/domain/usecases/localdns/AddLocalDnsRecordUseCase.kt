package eu.wedgess.piholecontrol.domain.usecases.localdns

import eu.wedgess.piholecontrol.domain.model.ModifyLocalDnsRecordResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import javax.inject.Inject

class AddLocalDnsRecordUseCase @Inject constructor(
    private val localDnsRepository: LocalDnsRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(
        ipAddress: String,
        domain: String
    ): Result<ModifyLocalDnsRecordResponseEntity> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                localDnsRepository.addLocalDnsRecord(
                    activeConnection = activeConnection,
                    ipAddress = ipAddress,
                    domain = domain
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
