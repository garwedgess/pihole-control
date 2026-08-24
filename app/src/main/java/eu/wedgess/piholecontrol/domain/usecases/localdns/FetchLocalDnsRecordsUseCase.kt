package eu.wedgess.piholecontrol.domain.usecases.localdns

import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import javax.inject.Inject

class FetchLocalDnsRecordsUseCase @Inject constructor(
    private val localDnsRepository: LocalDnsRepository,
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(): Result<List<LocalDnsRecordEntity>> {
        return connectionRepository.fetchActive().fold(
            onSuccess = { activeConnection ->
                localDnsRepository.fetchLocalDnsRecords(activeConnection)
            },
            onFailure = { error -> Result.failure(error) }
        )
    }
}
