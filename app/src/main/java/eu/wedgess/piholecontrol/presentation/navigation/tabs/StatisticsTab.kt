package eu.wedgess.piholecontrol.presentation.navigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.base.TabItem
import eu.wedgess.piholecontrol.utils.UiText

sealed class StatisticsTab(override val title: UiText, override val icon: ImageVector) :
    TabItem(title = title, icon = icon) {

    data object Queries : StatisticsTab(
        title = UiText.StringResource(R.string.statistics_tab_title_queries),
        icon = Icons.Default.QueryStats
    )

    data object Servers : StatisticsTab(
        title = UiText.StringResource(R.string.statistics_tab_title_servers),
        icon = Icons.Default.Dns
    )

    data object Domains : StatisticsTab(
        title = UiText.StringResource(R.string.statistics_tab_title_domains),
        icon = Icons.Default.Domain
    )

    data object Clients : StatisticsTab(
        title = UiText.StringResource(R.string.statistics_tab_title_clients),
        icon = Icons.Default.Devices
    )

    companion object {
        fun all() = listOf(Queries, Servers, Domains, Clients)
    }
}
