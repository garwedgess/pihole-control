package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.ConnectionVersion5
import eu.wedgess.piholecontrol.ConnectionVersion6
import eu.wedgess.piholecontrol.data.db.ConnectionVersion5Dao
import eu.wedgess.piholecontrol.data.db.ConnectionVersion6Dao
import eu.wedgess.piholecontrol.data.db.ConnectionViewDao
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toVersion5
import eu.wedgess.piholecontrol.data.mappers.toVersion6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class ConnectionRepositoryImpl(
    private val connectionVersion5Dao: ConnectionVersion5Dao,
    private val connectionVersion6Dao: ConnectionVersion6Dao,
    private val connectionViewDao: ConnectionViewDao,
    private val dispatcherProvider: DispatcherProvider
) : ConnectionRepository {

    override suspend fun insert(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            withVersionedDao(
                connection = connection,
                v5Call = { connectionVersion5Dao.insert(it) },
                v6Call = { connectionVersion6Dao.insert(it) }
            )
        }
    }

    override fun fetchAll(): Flow<Result<List<ConnectionEntity>>> =
        connectionViewDao.fetchAllAsFlow().map { list -> list.map { it.toEntity() } }
            .resultOf()

    override fun fetchActiveFlow(): Flow<Result<ConnectionEntity>> =
        connectionViewDao.fetchActiveFlow()
            .map { it?.toEntity() ?: ConnectionEntity.Version5.default }
            .resultOf()

    override suspend fun checkHasConnections(): Result<Boolean> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionViewDao.checkNotEmpty() }
        }

    override suspend fun fetchById(id: UUID): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionViewDao.fetchById(id).toEntity() }
        }

    override suspend fun fetchActive(): Result<ConnectionEntity> =
        withContext(dispatcherProvider.io) {
            resultOf { connectionViewDao.fetchActive().toEntity() }
        }

    override suspend fun update(
        connection: ConnectionEntity
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            withVersionedDao(
                connection = connection,
                v5Call = { connectionVersion5Dao.update(it) },
                v6Call = { connectionVersion6Dao.update(it) }
            )
        }
    }

    override suspend fun setActiveById(
        id: UUID
    ): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            withVersionedDao(
                id = id,
                versionCall = { version ->
                    when (version) {
                        PiHoleApiVersion.Version5 -> connectionVersion5Dao.setActive(id)
                        PiHoleApiVersion.Version6 -> connectionVersion6Dao.setActive(id)
                    }
                }
            )
        }
    }

    override suspend fun deleteById(id: UUID): Result<Unit> = withContext(dispatcherProvider.io) {
        resultOf {
            withVersionedDao(
                id = id,
                versionCall = { version ->
                    when (version) {
                        PiHoleApiVersion.Version5 -> connectionVersion5Dao.delete(id)
                        PiHoleApiVersion.Version6 -> connectionVersion6Dao.delete(id)
                    }
                }
            )
        }
    }

    override suspend fun deleteAllMarkedForDeletion(): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                connectionVersion5Dao.deleteMarkedForDeletion()
                connectionVersion6Dao.deleteMarkedForDeletion()
            }
        }

    override suspend fun markForDeletion(id: UUID): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                withVersionedDao(
                    id = id,
                    versionCall = { version ->
                        when (version) {
                            PiHoleApiVersion.Version5 -> connectionVersion5Dao.markAsDeleted(id)
                            PiHoleApiVersion.Version6 -> connectionVersion6Dao.markAsDeleted(id)
                        }
                    }
                )
            }
        }

    override suspend fun unmarkForDeletion(id: UUID): Result<Unit> =
        withContext(dispatcherProvider.io) {
            resultOf {
                withVersionedDao(
                    id = id,
                    versionCall = { version ->
                        when (version) {
                            PiHoleApiVersion.Version5 -> connectionVersion5Dao.unmarkAsDeleted(id)
                            PiHoleApiVersion.Version6 -> connectionVersion6Dao.unmarkAsDeleted(id)
                        }
                    }
                )
            }
        }

    private inline fun withVersionedDao(
        id: UUID,
        versionCall: (PiHoleApiVersion) -> Unit
    ) {
        val version = if (connectionVersion5Dao.exists(id)) {
            PiHoleApiVersion.Version5
        } else if (connectionVersion6Dao.exists(id)) {
            PiHoleApiVersion.Version6
        } else {
            throw IllegalArgumentException("No connection found with Id: $id")
        }
        versionCall(version)
    }
}

inline fun <T> withVersionedDao(
    connection: ConnectionEntity,
    v5Call: (ConnectionVersion5) -> T,
    v6Call: (ConnectionVersion6) -> T
): T {
    return when (connection) {
        is ConnectionEntity.Version5 -> v5Call(connection.toVersion5())
        is ConnectionEntity.Version6 -> v6Call(connection.toVersion6())
    }
}
