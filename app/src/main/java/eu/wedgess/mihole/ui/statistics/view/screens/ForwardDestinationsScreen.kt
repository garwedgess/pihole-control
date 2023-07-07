package eu.wedgess.mihole.ui.statistics.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.statistics.view.ForwardDestinationsContent
import eu.wedgess.mihole.utils.ColorGenerator

@Composable
fun ForwardDestinationsScreen(statistics: Resource<PiHoleStatistics>, colorGenerator: ColorGenerator) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (statistics) {
            is Resource.Loading -> LoadingContent(message = "Loading upstream servers")
            is Resource.Error -> ErrorMessage(errorMessage = statistics.errorMessage, onRetry = {})
            is Resource.Success -> ForwardDestinationsContent(
                forwardDestinations = statistics.data.forwardDestinations,
                colorGenerator
            )
        }
    }
}