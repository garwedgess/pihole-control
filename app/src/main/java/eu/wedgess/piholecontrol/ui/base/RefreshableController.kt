package eu.wedgess.piholecontrol.ui.base

import androidx.datastore.core.DataStore
import eu.wedgess.piholecontrol.data.db.ConnectionDao
import eu.wedgess.piholecontrol.data.model.ConnectionInfo
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.data.toConnectionInfo
import eu.wedgess.piholecontrol.utils.extensions.resultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


abstract class RefreshableController<T>(
    private val dao: ConnectionDao,
    private val userPreferences: DataStore<UserPreferences>
) {

    private val refreshFlow = RefreshFlow()

    abstract suspend fun fetchRefreshableData(activeConnectionInfo: ConnectionInfo): T

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenToDataChanges(overrideDelayMillis: Long? = null): Flow<Result<T>> =
        refreshFlow.flatMapLatest {
            combine(
                dao.fetchActiveFlow().map { it?.toConnectionInfo() ?: ConnectionInfo.default },
                userPreferences.data.map { it.refreshTime }
            ) { piHole, refreshDelay ->
                piHole to refreshDelay
            }
        }.flatMapLatest { (piHole, refreshDelay) ->
            flow {
                while (true) {
                    emit(fetchRefreshableData(piHole))
                    delay(overrideDelayMillis ?: refreshDelay)
                }
            }
        }.resultOf()

    fun triggerRefresh() = refreshFlow.refresh()

}
