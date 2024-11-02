package eu.wedgess.piholecontrol.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.piholecontrol.ui.app.model.AppBarState
import eu.wedgess.piholecontrol.ui.common.tabs.AnimatedTabContainer
import eu.wedgess.piholecontrol.ui.navigation.Screens
import eu.wedgess.piholecontrol.ui.navigation.StatisticsTab
import eu.wedgess.piholecontrol.ui.statquerytypes.navigation.QueryTypesRoot
import eu.wedgess.piholecontrol.ui.statserver.navigation.ServersRoot
import eu.wedgess.piholecontrol.ui.stattopclients.navigation.TopClientsScreenRoot
import eu.wedgess.piholecontrol.ui.stattopdomains.navigation.TopDomainsRoot

fun NavGraphBuilder.StatisticsDestination(onComposing: (AppBarState) -> Unit) {
    composable(
        route = Screens.Statistics.route,
    ) {

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