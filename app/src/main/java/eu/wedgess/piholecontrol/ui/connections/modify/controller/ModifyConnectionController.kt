package eu.wedgess.piholecontrol.ui.connections.modify.controller

import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.toConnection
import eu.wedgess.piholecontrol.data.toConnectionInfo
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModifyConnectionController @Inject constructor(
    private val piHolesDao: ConnectionDao,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun fetchConnection(id: Long) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.fetchById(id)?.toConnectionInfo() }
    }

    suspend fun saveConnection(connection: ConnectionInfo) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.insert(connection.toConnection()) }
    }

    suspend fun updateConnection(connection: ConnectionInfo) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.update(connection.toConnection()) }
    }
}