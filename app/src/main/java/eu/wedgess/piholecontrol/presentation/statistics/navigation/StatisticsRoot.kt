package eu.wedgess.piholecontrol.presentation.statistics.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.presentation.app.model.AppBarState
import eu.wedgess.piholecontrol.presentation.common.tabs.AnimatedTabContainer
import eu.wedgess.piholecontrol.presentation.navigation.Screens
import eu.wedgess.piholecontrol.presentation.navigation.tabs.StatisticsTab
import eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes.navigation.QueryTypesRoot
import eu.wedgess.piholecontrol.presentation.statistics.tabs.server.navigation.ServersRoot
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.navigation.TopClientsScreenRoot
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.navigation.TopDomainsRoot

fun NavGraphBuilder.statisticsRoot(onComposing: (AppBarState) -> Unit) {
    composable<Screens.Statistics> {
        LaunchedEffect(Unit) {
            onComposing(AppBarState(showSearchView = false, showNavigateBackIcon = false))
        }

        AnimatedTabContainer(tabItems = StatisticsTab.all()) {
            when (it) {
                StatisticsTab.Queries -> QueryTypesRoot()
                StatisticsTab.Servers -> ServersRoot()
                StatisticsTab.Domains -> TopDomainsRoot()
                StatisticsTab.Clients -> TopClientsScreenRoot()
            }
        }
    }
}
