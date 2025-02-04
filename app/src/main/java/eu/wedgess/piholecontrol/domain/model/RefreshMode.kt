package eu.wedgess.piholecontrol.domain.model

sealed class RefreshMode {
    data class Automatic(val refreshDelay: Long? = null) : RefreshMode()
    data object Manual : RefreshMode()
    data object None : RefreshMode()
}
