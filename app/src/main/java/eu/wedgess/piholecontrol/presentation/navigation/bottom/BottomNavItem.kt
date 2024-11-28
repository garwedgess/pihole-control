package eu.wedgess.piholecontrol.presentation.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.PlaylistAddCheck
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText

sealed class BottomNavItem(
    open val route: String,
    open val title: UiText,
    open val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Screens.Dashboard.route,
        UiText.StringResource(R.string.nav_title_home),
        Icons.Outlined.Home
    )

    object Statistics : BottomNavItem(
        route = Screens.Statistics.route,
        UiText.StringResource(R.string.nav_title_statistics),
        Icons.Outlined.Analytics
    )

    object Filters : BottomNavItem(
        route = Screens.Filters.route,
        UiText.StringResource(R.string.nav_title_filters),
        Icons.Outlined.LocalPolice
    )

    object Logs : BottomNavItem(
        route = Screens.Logs.route,
        UiText.StringResource(R.string.nav_title_logs),
        Icons.Outlined.PlaylistAddCheck
    )

    object Settings : BottomNavItem(
        route = Screens.Settings.route,
        UiText.StringResource(R.string.nav_title_settings),
        Icons.Outlined.Settings
    )

    companion object {
        fun all() = listOf(Home, Statistics, Filters, Logs, Settings)
    }
}