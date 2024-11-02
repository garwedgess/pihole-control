package eu.wedgess.piholecontrol.ui.connections.list.controller

import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConnectionsController @Inject constructor(
    private val piHolesDao: ConnectionDao,
    private val dispatcherProvider: DispatcherProvider
) {

    fun fetchAll() = piHolesDao.fetchAllAsFlow().resultOf()

    suspend fun setConnectionAsActive(connectionId: Long) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.setActive(connectionId) }
    }
}