package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.domain.mappers.toConnection
import eu.wedgess.piholecontrol.domain.mappers.toConnectionInfo
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ConnectionRepositoryImpl(
    private val connectionDao: ConnectionDao,
    private val dispatcherProvider: DispatcherProvider
) : ConnectionRepository {

    override suspend fun insert(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.insert(connection.toConnection())
        }
    }

    override fun fetchAll(): Flow<Result<List<ConnectionEntity>>> =
        connectionDao.fetchAllAsFlow().map { list -> list.map { it.toConnectionInfo() } }.resultOf()

    override fun fetchActiveFlow(): Flow<Result<ConnectionEntity>> =
        connectionDao.fetchActiveFlow().map { it?.toConnectionInfo() ?: ConnectionEntity.default }
            .resultOf()

    override suspend fun checkHasConnections(): Result<Boolean> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.checkNotEmpty() }
        }

    override suspend fun fetchById(id: Long): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchById(id).toConnectionInfo() }
        }

    override suspend fun fetchActive(): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchActive().toConnectionInfo() }
        }

    override suspend fun update(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.update(connection.toConnection())
        }
    }

    override suspend fun setActiveById(
        id: Long
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.setActive(id)
        }
    }

    override suspend fun deleteById(id: Long): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.delete(id)
        }
    }

    override suspend fun deleteAllMarkedForDeletion(): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.deleteMarkedForDeletion()
        }
    }

    override suspend fun markForDeletion(id: Long): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.markAsDeleted(id)
        }
    }

    override suspend fun unmarkForDeletion(id: Long): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.unmarkAsDeleted(id)
        }
    }
}
