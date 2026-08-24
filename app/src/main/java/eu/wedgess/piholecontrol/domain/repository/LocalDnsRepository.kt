package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordEntity
import eu.wedgess.piholecontrol.domain.model.LocalDnsRecordUpdateEntity
import eu.wedgess.piholecontrol.domain.model.ModifyLocalDnsRecordResponseEntity

interface LocalDnsRepository {

    suspend fun fetchLocalDnsRecords(
        activeConnection: ConnectionEntity
    ): Result<List<LocalDnsRecordEntity>>

    suspend fun addLocalDnsRecord(
        activeConnection: ConnectionEntity,
        ipAddress: String,
        domain: String
    ): Result<ModifyLocalDnsRecordResponseEntity>

    suspend fun updateLocalDnsRecord(
        activeConnection: ConnectionEntity,
        update: LocalDnsRecordUpdateEntity
    ): Result<ModifyLocalDnsRecordResponseEntity>

    suspend fun deleteLocalDnsRecord(
        activeConnection: ConnectionEntity,
        value: String
    ): Result<ModifyLocalDnsRecordResponseEntity>
}
