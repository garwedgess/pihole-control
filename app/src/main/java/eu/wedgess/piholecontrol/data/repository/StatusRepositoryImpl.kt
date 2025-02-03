package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.StatusApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.StatusApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toStatusEntity
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import eu.wedgess.piholecontrol.domain.repository.StatusRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext
import kotlin.time.Duration

class StatusRepositoryImpl(
    private val apiV5: StatusApiServiceV5,
    private val apiV6: StatusApiServiceV6,
    private val dispatcherProvider: DispatcherProvider
) : StatusRepository {

    override suspend fun fetchStatus(activeConnection: ConnectionEntity): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.fetchStatus(it) },
                v6Call = { apiV6.fetchStatus(it) },
                mapper = {
                    when (it) {
                        is PiHoleStatusResponseDataV5 -> it.toStatusEntity()
                        is PiHoleStatusResponseDataV6 -> it.toStatusEntity()
                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }

    override suspend fun enableAdBlocking(activeConnection: ConnectionEntity): Result<StatusEntity> =
        withContext(dispatcherProvider.io) {
            callVersionedEndpoint(
                activeConnection = activeConnection,
                v5Call = { apiV5.enableAdBlocking(it) },
                v6Call = { apiV6.enableAdBlocking(it) },
                mapper = {
                    when (it) {
                        is PiHoleStatusResponseDataV5 -> it.toStatusEntity()
                        is PiHoleStatusResponseDataV6 -> it.toStatusEntity()
                        else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                    }
                }
            )
        }

    override suspend fun disableAdBlocking(
        activeConnection: ConnectionEntity,
        duration: Duration
    ): Result<StatusEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeConnection,
            v5Call = { apiV5.disableAdBlocking(it, duration) },
            v6Call = { apiV6.disableAdBlocking(it, duration) },
            mapper = {
                when (it) {
                    is PiHoleStatusResponseDataV5 -> it.toStatusEntity()
                    is PiHoleStatusResponseDataV6 -> it.toStatusEntity()
                    else -> throw IllegalArgumentException("Unknown type: ${it.javaClass.name}")
                }
            }
        )
    }
}
