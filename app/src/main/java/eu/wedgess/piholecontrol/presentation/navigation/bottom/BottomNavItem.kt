package eu.wedgess.piholecontrol.presentation.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistAddCheck
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.utils.UiText
import kotlinx.serialization.Serializable

sealed class BottomNavItem<T>(
    @Serializable open val route: T,
    open val title: UiText,
    open val icon: ImageVector
) {
    data object Home : BottomNavItem<Screens.Dashboard>(
        route = Screens.Dashboard,
        UiText.StringResource(R.string.nav_title_home),
        Icons.Outlined.Home
    )

    data object Statistics : BottomNavItem<Screens.Statistics>(
        route = Screens.Statistics,
        UiText.StringResource(R.string.nav_title_statistics),
        Icons.Outlined.Analytics
    )

    data object Filters : BottomNavItem<Screens.Filters>(
        route = Screens.Filters,
        UiText.StringResource(R.string.nav_title_filters),
        Icons.Outlined.LocalPolice
    )

    data object Logs : BottomNavItem<Screens.Logs>(
        route = Screens.Logs,
        UiText.StringResource(R.string.nav_title_logs),
        Icons.AutoMirrored.Outlined.PlaylistAddCheck
    )

    data object Settings : BottomNavItem<Screens.Settings>(
        route = Screens.Settings,
        UiText.StringResource(R.string.nav_title_settings),
        Icons.Outlined.Settings
    )

    companion object {
        fun all() = listOf(Home, Statistics, Filters, Logs, Settings)
    }
}
