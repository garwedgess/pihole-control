package eu.wedgess.mihole.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.wedgess.mihole.data.PiHoleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

abstract class RefreshableViewModel(
    private val repository: PiHoleRepository
) : ViewModel() {

    private var refreshJob: Job? = null

    protected abstract fun onRefresh()

    init {

    }

    private suspend fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch { onRefresh() }
        refreshJob?.join()
    }

    protected fun listenForConnectionChange() {
        viewModelScope.launch {
//            repository.fetchActiveFlow()
//                .collectLatest {
//                    refresh()
//                }
        }
    }

    fun autoRefreshData(): Job = viewModelScope.launch {
        val refreshDelay = repository.fetchUserPreferences().firstOrNull()?.refreshTime
            ?: TimeUnit.SECONDS.toMillis(10)
        do {
            delay(refreshDelay)
            refresh()
        } while (true)
    }
}

