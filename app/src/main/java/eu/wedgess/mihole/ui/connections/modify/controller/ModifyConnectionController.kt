package eu.wedgess.mihole.ui.connections.modify.controller

import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.toPiHole
import eu.wedgess.mihole.data.toPiHoleInfo
import eu.wedgess.mihole.utils.DispatcherProvider
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModifyConnectionController @Inject constructor(
    private val piHolesDao: MiHolesDao,
    private val dispatcherProvider: DispatcherProvider
) {

    suspend fun fetchConnection(id: Long) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.fetchById(id)?.toPiHoleInfo() }
    }

    suspend fun saveConnection(connection: PiHoleInfo) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.insert(connection.toPiHole()) }
    }

    suspend fun updateConnection(connection: PiHoleInfo) = withContext(dispatcherProvider.io) {
        resultOf { piHolesDao.update(connection.toPiHole()) }
    }
}