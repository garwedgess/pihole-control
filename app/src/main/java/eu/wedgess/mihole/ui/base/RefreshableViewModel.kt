package eu.wedgess.mihole.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.wedgess.mihole.data.PiHoleRepository
import eu.wedgess.mihole.data.model.PiHoleInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

abstract class RefreshableViewModel(
    private val repository: PiHoleRepository
) : ViewModel() {

    private val _activePiHole = MutableStateFlow(PiHoleInfo.default)
    protected val activePiHole: StateFlow<PiHoleInfo> = _activePiHole
    private var refreshJob: Job? = null
    private var connectionListenerJob: Job? = null

    protected abstract fun onRefresh()

    private suspend fun refresh() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch { onRefresh() }
        refreshJob?.join()
    }

    protected fun listenForConnectionChange() {
        connectionListenerJob?.cancel()
        connectionListenerJob = viewModelScope.launch {
            repository.fetchActiveFlow()
                .collectLatest {
                    refresh()
                }
        }
    }

    protected fun stopListeningForConnectionChange() = connectionListenerJob?.cancel()

    protected fun autoRefreshData(): Job = viewModelScope.launch {
        val refreshDelay = repository.fetchUserPreferences().firstOrNull()?.refreshTime
            ?: TimeUnit.SECONDS.toMillis(10)

        while (isActive) {
            delay(refreshDelay)
            refresh()
        }
    }
}

