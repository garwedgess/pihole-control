package eu.wedgess.piholecontrol.ui.statistics.tabs.topdomains.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueries
import eu.wedgess.piholecontrol.ui.compose.Compose
import eu.wedgess.piholecontrol.ui.compose.ErrorScreen
import eu.wedgess.piholecontrol.ui.compose.LoadingScreen
import eu.wedgess.piholecontrol.ui.compose.UIResult

@Composable
fun TopDomainsScreen(statistics: UIResult<PiHoleTopQueries>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statistics.Compose(
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
            onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
            onLoaded = {
                TopDomainsContent(
                    topPermittedDomains = it.topQueries,
                    topBlockedDomains = it.topAds
                )
            }
        )
    }
}