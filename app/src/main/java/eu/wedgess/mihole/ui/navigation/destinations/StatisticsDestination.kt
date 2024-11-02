package eu.wedgess.mihole.ui.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.wedgess.mihole.ui.app.model.AppBarState
import eu.wedgess.mihole.ui.common.tabs.AnimatedTabContainer
import eu.wedgess.mihole.ui.navigation.Screens
import eu.wedgess.mihole.ui.navigation.StatisticsTab
import eu.wedgess.mihole.ui.statquerytypes.navigation.QueryTypesRoot
import eu.wedgess.mihole.ui.statserver.navigation.ServersRoot
import eu.wedgess.mihole.ui.stattopclients.navigation.TopClientsScreenRoot
import eu.wedgess.mihole.ui.stattopdomains.navigation.TopDomainsRoot

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