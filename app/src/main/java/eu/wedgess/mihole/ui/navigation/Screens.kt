package eu.wedgess.mihole.ui.navigation

sealed class Screens(val route: String) {
    object Dashboard: Screens(route = "dashboard")
    object Statistics: Screens(route = "statistics")
    object Filters: Screens(route = "filters")
    object Logs: Screens(route = "logs")
}