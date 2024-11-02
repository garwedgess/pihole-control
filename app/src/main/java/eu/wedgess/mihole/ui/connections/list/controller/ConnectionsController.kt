package eu.wedgess.mihole.ui.connections.list.controller

import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.utils.DispatcherProvider
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConnectionsController @Inject constructor(
    private val piHolesDao: MiHolesDao,
    private val dispatcherProvider: DispatcherProvider
) {

    fun fetchAll() = piHolesDao.fetchAllAsFlow().resultOf()

    suspend fun setConnectionAsActive(connectionId: Long) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.setActive(connectionId) }
    }
}