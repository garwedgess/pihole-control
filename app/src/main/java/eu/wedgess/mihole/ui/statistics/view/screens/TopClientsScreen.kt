package eu.wedgess.mihole.ui.statistics.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.statistics.view.content.TopClientsContent

@Composable
fun TopClientsScreen(statistics: UiResult<PiHoleStatistics>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (statistics) {
            is UiResult.Loading -> LoadingContent(message = "Loading query types")
            is UiResult.Error -> ErrorMessage(errorMessage = statistics.errorMessage, onRetry = {})
            is UiResult.Success -> TopClientsContent(
                topClients = statistics.data.topSources.map { (key, value) -> Pair(key, value) }
            )
        }
    }
}