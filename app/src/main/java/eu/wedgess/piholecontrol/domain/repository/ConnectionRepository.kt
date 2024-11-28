package eu.wedgess.piholecontrol.domain.repository

import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {
    suspend fun insert(connection: ConnectionInfo): Result<Unit>
    fun fetchAll(): Flow<Result<List<ConnectionInfo>>>
    fun fetchActiveFlow(): Flow<Result<ConnectionInfo>>
    suspend fun fetchById(id: Long): Result<ConnectionInfo>
    suspend fun fetchActive(): Result<ConnectionInfo>
    suspend fun update(connection: ConnectionInfo): Result<Unit>
    suspend fun setActiveById(id: Long): Result<Unit>
    suspend fun deleteById(id: Long): Result<Unit>
}