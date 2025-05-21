package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.mappers.toData
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ConnectionRepositoryImpl @Inject constructor(
    private val connectionDao: ConnectionDao,
    private val dispatcherProvider: DispatcherProvider
) : ConnectionRepository {

    override suspend fun insert(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf { connectionDao.insert(connection.toData()) }
    }

    override fun fetchAll(): Flow<Result<List<ConnectionEntity>>> =
        connectionDao.fetchAllAsFlow().map { list -> list.map { it.toEntity() } }
            .resultOf()

    override fun fetchActiveFlow(): Flow<Result<ConnectionEntity>> =
        connectionDao.fetchActiveFlow()
            .map { it?.toEntity() ?: ConnectionEntity.default }
            .resultOf()

    override suspend fun checkHasConnections(): Result<Boolean> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.checkNotEmpty() }
        }

    override suspend fun fetchById(id: UUID): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchById(id).toEntity() }
        }

    override suspend fun fetchActive(): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchActive()?.toEntity() ?: ConnectionEntity.default }
        }

    override suspend fun update(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.update(connection.toData())
        }
    }

    override suspend fun setActiveById(
        id: UUID
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.setActive(id)
        }
    }

    override suspend fun deleteById(id: UUID): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.delete(id)
        }
    }

    override suspend fun deleteAllMarkedForDeletion(): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                connectionDao.deleteMarkedForDeletion()
            }
        }

    override suspend fun markForDeletion(id: UUID): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                connectionDao.markAsDeleted(id)
            }
        }

    override suspend fun unmarkForDeletion(id: UUID): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                connectionDao.unmarkAsDeleted(id)
            }
        }
}
