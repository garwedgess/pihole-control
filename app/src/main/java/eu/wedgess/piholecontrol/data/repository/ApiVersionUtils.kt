package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.domain.model.ConnectionEntity

suspend inline fun <T, R> callVersionedEndpoint(
    activeConnection: ConnectionEntity,
    crossinline v5Call: suspend (ConnectionEntity.Version5) -> Result<T>,
    crossinline v6Call: suspend (ConnectionEntity.Version6) -> Result<T>,
    crossinline mapper: (T) -> R
): Result<R> {
    return when (activeConnection) {
        is ConnectionEntity.Version5 -> v5Call(activeConnection)
        is ConnectionEntity.Version6 -> v6Call(activeConnection)
    }.mapCatching {
        mapper(it)
    }
}
