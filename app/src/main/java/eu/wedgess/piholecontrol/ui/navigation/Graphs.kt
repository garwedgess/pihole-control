package eu.wedgess.piholecontrol.ui.navigation

sealed class Graphs(val route: String) {
    object Main: Graphs("main_graph")
    object Settings: Graphs("settings_graph")
}