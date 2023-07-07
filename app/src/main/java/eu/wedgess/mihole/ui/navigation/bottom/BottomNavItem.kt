package eu.wedgess.mihole.ui.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.ui.navigation.Screens

sealed class BottomNavItem(
    open val route: String,
    open val title: String,
    open val icon: ImageVector
) {
    object Home: BottomNavItem(route = Screens.Dashboard.route, "Home", Icons.Default.Home)
    object Statistics: BottomNavItem(route = Screens.Statistics.route, "Statistics", Icons.Default.Analytics)
    object Filters: BottomNavItem(route = Screens.Filters.route, "Filters", Icons.Default.LocalPolice)

    companion object {
        fun all() = listOf(Home, Statistics, Filters)
    }
}