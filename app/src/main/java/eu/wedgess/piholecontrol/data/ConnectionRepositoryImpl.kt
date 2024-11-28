package eu.wedgess.piholecontrol.data

import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
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
        connection: ConnectionInfo
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            connectionDao.insert(connection.toConnection())
        }
    }

    override fun fetchAll(): Flow<Result<List<ConnectionInfo>>> =
        connectionDao.fetchAllAsFlow().map { list -> list.map { it.toConnectionInfo() } }.resultOf()

    override fun fetchActiveFlow(): Flow<Result<ConnectionInfo>> =
        connectionDao.fetchActiveFlow().map { it.toConnectionInfo() }.resultOf()

    override suspend fun fetchById(id: Long): Result<ConnectionInfo> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchById(id).toConnectionInfo() }
        }

    override suspend fun fetchActive(): Result<ConnectionInfo> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionDao.fetchActive().toConnectionInfo() }
        }

    override suspend fun update(
        connection: ConnectionInfo
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
}
