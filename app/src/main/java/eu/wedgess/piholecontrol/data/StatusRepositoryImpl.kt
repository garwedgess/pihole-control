package eu.wedgess.piholecontrol.data

import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.domain.mappers.toStatusEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import kotlin.time.Duration

class StatusRepositoryImpl(
    private val api: StatusApiService,
    private val dispatcherProvider: DispatcherProvider
) : StatusRepository {

    override suspend fun fetchStatus(activeConnection: ConnectionInfo): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            api.fetchStatus(activeConnection).mapCatching { it.toStatusEntity() }
        }

    override suspend fun enableAdBlocking(activeConnection: ConnectionInfo): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            api.enableAdBlocking(activeConnection).mapCatching { it.toStatusEntity() }
        }

    override suspend fun disableAdBlocking(
        activeConnection: ConnectionInfo,
        duration: Duration
    ): Result<StatusEntity> = withContext(dispatcherProvider.io) {
        api.disableAdBlocking(activeConnection, duration).mapCatching { it.toStatusEntity() }
    }


}