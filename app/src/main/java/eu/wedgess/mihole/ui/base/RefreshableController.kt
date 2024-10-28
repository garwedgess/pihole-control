package eu.wedgess.mihole.ui.base

import androidx.datastore.core.DataStore
import eu.wedgess.mihole.data.db.MiHolesDao
import eu.wedgess.mihole.data.model.PiHoleInfo
import eu.wedgess.mihole.data.model.UserPreferences
import eu.wedgess.mihole.data.toMiHoleInfo
import eu.wedgess.mihole.utils.extensions.resultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber


abstract class RefreshableController<T>(
    private val dao: MiHolesDao,
    private val userPreferences: DataStore<UserPreferences>
) {

    private val refreshFlow = RefreshFlow()

    abstract suspend fun fetchData(activePiHoleInfo: PiHoleInfo): T

    @OptIn(ExperimentalCoroutinesApi::class)
    fun listenToDataChanges(overrideDelayMillis: Long? = null): Flow<Result<T>> =
        refreshFlow.flatMapLatest {
            combine(
                dao.fetchActiveFlow().map { it?.toMiHoleInfo() ?: PiHoleInfo.default },
                userPreferences.data.map { it.refreshTime }
            ) { piHole, refreshDelay ->
                piHole to refreshDelay
            }
        }.flatMapLatest { (piHole, refreshDelay) ->
            flow {
                while (true) {
                    Timber.d("Autorefresh")
                    emit(fetchData(piHole))
                    delay(overrideDelayMillis ?: refreshDelay)
                }
            }
        }.resultOf()

    fun triggerRefresh() = refreshFlow.refresh()

}
