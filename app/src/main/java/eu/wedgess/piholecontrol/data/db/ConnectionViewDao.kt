package eu.wedgess.piholecontrol.data.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import eu.wedgess.piholecontrol.data.PiHoleControlDatabase
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import java.util.UUID
import javax.inject.Inject

class ConnectionViewDao @Inject constructor(
    db: PiHoleControlDatabase,
    private val dispatcherProvider: DispatcherProvider
) {
    private val queries = db.connectionViewQueries

    fun fetchAllAsFlow() = queries.selectAll().asFlow().mapToList(dispatcherProvider.io)

    fun fetchById(id: UUID) = queries.selectById(id).executeAsOne()

    fun fetchActive() = queries.selectActive().executeAsOne()

    fun fetchActiveFlow() = queries.selectActive().asFlow()
        .mapToOneOrNull(dispatcherProvider.io)

    fun checkNotEmpty(): Boolean = queries.checkNotEmpty().executeAsOne()
}
