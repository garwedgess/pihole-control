package eu.wedgess.mihole.ui.statistics.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.common.tabs.AnimatedTabContainer
import eu.wedgess.mihole.ui.base.TabItem
import eu.wedgess.mihole.ui.statistics.StatisticsContract
import eu.wedgess.mihole.ui.statistics.view.screens.ForwardDestinationsScreen
import eu.wedgess.mihole.ui.statistics.view.screens.QueryTypesScreen
import eu.wedgess.mihole.ui.statistics.view.screens.TopClientsScreen
import eu.wedgess.mihole.ui.statistics.view.screens.TopDomainsScreen
import eu.wedgess.mihole.utils.ColorGenerator
import eu.wedgess.mihole.utils.UiText

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
                title = UiText.StringResource(R.string.statistics_tab_title_queries),
                icon = Icons.Default.QueryStats,
                screen = { QueryTypesScreen(uiState.statistics, colorGenerator) }
            ),
            TabItem(
                title = UiText.StringResource(R.string.statistics_tab_title_servers),
                icon = Icons.Default.Dns,
                screen = { ForwardDestinationsScreen(uiState.statistics, colorGenerator) }
            ),
            TabItem(
                title = UiText.StringResource(R.string.statistics_tab_title_domains),
                icon = Icons.Default.Domain,
                screen = { TopDomainsScreen(uiState.statistics) }
            ),
            TabItem(
                title = UiText.StringResource(R.string.statistics_tab_title_clients),
                icon = Icons.Default.Devices,
                screen = { TopClientsScreen(uiState.statistics) }
            )
        )
    }
    AnimatedTabContainer(tabItems = tabItems)
}