package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.LocalDnsApiService
import eu.wedgess.piholecontrol.data.mappers.formatLocalDnsRecordValue
import eu.wedgess.piholecontrol.data.mappers.toLocalDnsRecordEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordEntity
import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordUpdateEntity
import eu.wedgess.piholecontrol.domain.model.ModifyLocalDnsRecordResponseEntity
import eu.wedgess.piholecontrol.domain.repository.LocalDnsRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class LocalDnsRepositoryImpl(
    private val localDnsApiService: LocalDnsApiService,
    private val dispatcherProvider: DispatcherProvider
) : LocalDnsRepository {

    override suspend fun fetchLocalDnsRecords(
        activeConnection: ConnectionEntity
    ): Result<List<LocalDnsRecordEntity>> = withContext(dispatcherProvider.io) {
        localDnsApiService.fetchLocalDnsRecords(activeConnection)
            .mapCatching { response ->
                response.config.dns.hosts
                    .mapNotNull { it.toLocalDnsRecordEntity() }
                    .sortedWith(compareBy({ it.domain.lowercase() }, { it.ipAddress }))
            }
    }

    override suspend fun addLocalDnsRecord(
        activeConnection: ConnectionEntity,
        ipAddress: String,
        domain: String
    ): Result<ModifyLocalDnsRecordResponseEntity> = withContext(dispatcherProvider.io) {
        localDnsApiService.addLocalDnsRecord(
            connection = activeConnection,
            value = formatLocalDnsRecordValue(ipAddress, domain)
        ).mapCatching { successModifyLocalDnsRecordResponse() }
    }

    override suspend fun updateLocalDnsRecord(
        activeConnection: ConnectionEntity,
        update: LocalDnsRecordUpdateEntity
    ): Result<ModifyLocalDnsRecordResponseEntity> = withContext(dispatcherProvider.io) {
        val newValue = formatLocalDnsRecordValue(update.ipAddress, update.domain)
        if (update.originalValue == newValue) {
            Result.success(successModifyLocalDnsRecordResponse())
        } else {
            replaceLocalDnsRecord(
                activeConnection = activeConnection,
                originalValue = update.originalValue,
                newValue = newValue
            )
        }
    }

    override suspend fun deleteLocalDnsRecord(
        activeConnection: ConnectionEntity,
        value: String
    ): Result<ModifyLocalDnsRecordResponseEntity> = withContext(dispatcherProvider.io) {
        localDnsApiService.deleteLocalDnsRecord(
            connection = activeConnection,
            value = value
        ).mapCatching { successModifyLocalDnsRecordResponse() }
    }

    private suspend fun replaceLocalDnsRecord(
        activeConnection: ConnectionEntity,
        originalValue: String,
        newValue: String
    ): Result<ModifyLocalDnsRecordResponseEntity> {
        return localDnsApiService.addLocalDnsRecord(
            connection = activeConnection,
            value = newValue
        ).fold(
            onSuccess = {
                localDnsApiService.deleteLocalDnsRecord(
                    connection = activeConnection,
                    value = originalValue
                ).mapCatching { successModifyLocalDnsRecordResponse() }
            },
            onFailure = { error -> Result.failure(error) }
        )
    }

    private fun successModifyLocalDnsRecordResponse() = ModifyLocalDnsRecordResponseEntity(
        success = true,
        message = null
    )
}
