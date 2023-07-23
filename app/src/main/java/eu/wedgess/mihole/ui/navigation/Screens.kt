package eu.wedgess.mihole.ui.navigation

const val KEY_ARG_ID = "id"
private const val CONNECTION_DETAILS_ROUTE = "modify_connection?${KEY_ARG_ID}={${KEY_ARG_ID}}"

sealed class Screens(val route: String) {
    object Dashboard: Screens(route = "dashboard")
    object Statistics: Screens(route = "statistics")
    object Filters: Screens(route = "filters")
    object Logs: Screens(route = "logs")
    object Settings: Screens(route = "settings")
    object Connections: Screens(route = "connections")
    object ModifyConnection: Screens(route = CONNECTION_DETAILS_ROUTE) {
        fun routeWithArgs(id: Long) =  route.replace("{$KEY_ARG_ID}", id.toString())
        fun routeWithNoArgs() =  route.substringBefore("?")
    }
}