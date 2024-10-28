package eu.wedgess.mihole.ui.navigation

const val KEY_ARG_ID = "id"
private const val CONNECTION_DETAILS_ROUTE = "modify_connection?${KEY_ARG_ID}={${KEY_ARG_ID}}"

sealed class Screens(val route: String) {
    data object Dashboard: Screens(route = "dashboard")
    data object Statistics: Screens(route = "statistics")
    data object QueryTypes: Screens(route = "querytypes")
    data object Servers: Screens(route = "servers")
    data object TopQueries: Screens(route = "top_queries")
    data object TopClients: Screens(route = "top_clients")
    data object Filters: Screens(route = "filters")
    data object Logs: Screens(route = "logs")
    data object Settings: Screens(route = "settings")
    data object Connections: Screens(route = "connections")
    data object ModifyConnection: Screens(route = CONNECTION_DETAILS_ROUTE) {
        fun routeWithArgs(id: Long) =  route.replace("{$KEY_ARG_ID}", id.toString())
        fun routeWithNoArgs() =  route.substringBefore("?")
    }
}