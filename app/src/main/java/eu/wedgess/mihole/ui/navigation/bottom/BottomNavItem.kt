package eu.wedgess.mihole.ui.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.utils.UiText

sealed class BottomNavItem(
    open val route: String,
    open val title: UiText,
    open val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screens.Dashboard.route,
        UiText.StringResource(R.string.nav_title_home),
        Icons.Default.Home
    )

    object Statistics : BottomNavItem(
        route = Screens.Statistics.route,
        UiText.StringResource(R.string.nav_title_statistics),
        Icons.Default.Analytics
    )

    object Filters : BottomNavItem(
        route = Screens.Filters.route,
        UiText.StringResource(R.string.nav_title_filters),
        Icons.Default.LocalPolice
    )

    object Logs : BottomNavItem(
        route = Screens.Logs.route,
        UiText.StringResource(R.string.nav_title_logs),
        Icons.Default.PlaylistAddCheck
    )

    companion object {
        fun all() = listOf(Home, Statistics, Filters, Logs)
    }
}