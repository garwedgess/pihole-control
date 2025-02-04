package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ConnectionRepository {
    suspend fun insert(connection: ConnectionEntity): Result<Unit>
    fun fetchAll(): Flow<Result<List<ConnectionEntity>>>
    fun fetchActiveFlow(): Flow<Result<ConnectionEntity>>
    suspend fun checkHasConnections(): Result<Boolean>
    suspend fun fetchById(id: UUID): Result<ConnectionEntity>
    suspend fun fetchActive(): Result<ConnectionEntity>
    suspend fun update(connection: ConnectionEntity): Result<Unit>
    suspend fun setActiveById(id: UUID): Result<Unit>
    suspend fun deleteById(id: UUID): Result<Unit>
    suspend fun deleteAllMarkedForDeletion(): Result<Unit>
    suspend fun markForDeletion(id: UUID): Result<Unit>
    suspend fun unmarkForDeletion(id: UUID): Result<Unit>
}
