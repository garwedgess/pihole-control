package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.StatusApiService
import eu.wedgess.piholecontrol.data.mappers.toStatusEntity
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import kotlin.time.Duration

class StatusRepositoryImpl(
    private val statusApiService: StatusApiService,
    private val dispatcherProvider: DispatcherProvider
) : StatusRepository {

    override suspend fun fetchStatus(activeConnection: ConnectionEntity): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            statusApiService.fetchStatus(activeConnection).mapCatching { it.toStatusEntity() }
        }

    override suspend fun enableAdBlocking(activeConnection: ConnectionEntity): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            statusApiService.enableAdBlocking(activeConnection).mapCatching {
                it.toStatusEntity()
            }
        }

    override suspend fun disableAdBlocking(
        activeConnection: ConnectionEntity,
        duration: Duration
    ): Result<StatusEntity> = withContext(dispatcherProvider.io) {
        statusApiService.disableAdBlocking(activeConnection, duration).mapCatching {
            it.toStatusEntity()
        }
    }
}
