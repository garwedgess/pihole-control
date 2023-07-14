package eu.wedgess.mihole.ui.statistics.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import eu.wedgess.mihole.ui.common.tabs.AnimatedTabContainer
import eu.wedgess.mihole.ui.common.tabs.TabItem
import eu.wedgess.mihole.ui.statistics.StatisticsContract
import eu.wedgess.mihole.ui.statistics.view.screens.ForwardDestinationsScreen
import eu.wedgess.mihole.ui.statistics.view.screens.QueryTypesScreen
import eu.wedgess.mihole.ui.statistics.view.screens.TopClientsScreen
import eu.wedgess.mihole.ui.statistics.view.screens.TopDomainsScreen
import eu.wedgess.mihole.utils.ColorGenerator

@Composable
fun StatisticsScreen(
    uiState: StatisticsContract.UiState,
    onEvent: (StatisticsContract.Event) -> Unit
) {
    val darkTheme = isSystemInDarkTheme()
    val colorGenerator = remember {
        ColorGenerator(isLightTheme = !darkTheme)
    }


    val tabItems = remember(uiState.statistics) {
        mutableListOf(
            TabItem(
                title = "Query Types",
                icon = Icons.Default.QueryStats,
                screen = { QueryTypesScreen(uiState.statistics, colorGenerator) }
            ),
            TabItem(
                title = "Servers",
                icon = Icons.Default.Dns,
                screen = { ForwardDestinationsScreen(uiState.statistics, colorGenerator) }
            ),
            TabItem(
                title = "Domains",
                icon = Icons.Default.Domain,
                screen = { TopDomainsScreen(uiState.statistics) }
            ),
            TabItem(
                title = "Clients",
                icon = Icons.Default.Devices,
                screen = { TopClientsScreen(uiState.statistics) }
            )
        )
    }
    AnimatedTabContainer(tabItems = tabItems)
}