package eu.wedgess.piholecontrol.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object Dashboard : Screens

    @Serializable
    data object Statistics : Screens

    @Serializable
    data object Filters : Screens

    @Serializable
    data object Logs : Screens

    @Serializable
    data object Settings : Screens

    @Serializable
    data object Connections : Screens

    @Serializable
    data class ModifyConnection(val connectionId: String? = null) : Screens
}
