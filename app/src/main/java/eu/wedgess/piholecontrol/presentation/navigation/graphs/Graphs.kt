package eu.wedgess.piholecontrol.presentation.navigation.graphs

import kotlinx.serialization.Serializable

sealed interface Graphs {
    @Serializable data object Tools : Graphs
}
