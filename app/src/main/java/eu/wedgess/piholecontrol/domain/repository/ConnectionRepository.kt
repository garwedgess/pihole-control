package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {
    suspend fun insert(connection: ConnectionEntity): Result<Unit>
    fun fetchAll(): Flow<Result<List<ConnectionEntity>>>
    fun fetchActiveFlow(): Flow<Result<ConnectionEntity>>
    suspend fun checkHasConnections(): Result<Boolean>
    suspend fun fetchById(id: Long): Result<ConnectionEntity>
    suspend fun fetchActive(): Result<ConnectionEntity>
    suspend fun update(connection: ConnectionEntity): Result<Unit>
    suspend fun setActiveById(id: Long): Result<Unit>
    suspend fun deleteById(id: Long): Result<Unit>
}
