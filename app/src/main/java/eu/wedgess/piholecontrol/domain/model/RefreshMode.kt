package eu.wedgess.piholecontrol.domain.model

sealed class RefreshMode {
    data object Automatic : RefreshMode()
    data object Manual : RefreshMode()
    data object None : RefreshMode()
}
