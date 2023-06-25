package eu.wedgess.mihole.ui.navigation

sealed class Screens(val route: String) {
    object Dashboard: Screens(route = "dashboard")
}