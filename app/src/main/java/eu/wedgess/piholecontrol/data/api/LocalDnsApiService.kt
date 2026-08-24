package eu.wedgess.piholecontrol.data.api

import eu.wedgess.piholecontrol.data.model.responses.PiHoleApiResult
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLocalDnsResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

interface LocalDnsApiService {

    suspend fun fetchLocalDnsRecords(
        connection: ConnectionEntity
    ): PiHoleApiResult<PiHoleLocalDnsResponseData>

    suspend fun addLocalDnsRecord(
        connection: ConnectionEntity,
        value: String
    ): PiHoleApiResult<Unit>

    suspend fun deleteLocalDnsRecord(
        connection: ConnectionEntity,
        value: String
    ): PiHoleApiResult<Unit>
}
