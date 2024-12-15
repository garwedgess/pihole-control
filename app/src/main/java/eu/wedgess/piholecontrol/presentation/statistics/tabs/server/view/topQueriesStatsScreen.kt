package eu.wedgess.piholecontrol.presentation.statistics.tabs.server.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.wedgess.piholecontrol.domain.model.ForwardDestinationEntity
import eu.wedgess.piholecontrol.presentation.compose.Compose
import eu.wedgess.piholecontrol.presentation.compose.ErrorScreen
import eu.wedgess.piholecontrol.presentation.compose.LoadingScreen
import eu.wedgess.piholecontrol.presentation.compose.UIResult

@Composable
fun ServersScreen(statistics: UIResult<List<ForwardDestinationEntity>>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        statistics.Compose(
            onLoading = { LoadingScreen(modifier = Modifier.fillMaxSize(), it) },
            onError = { ErrorScreen(modifier = Modifier.fillMaxSize(), it) },
            onLoaded = { ForwardDestinationsContent(forwardDestinations = it) }
        )
    }
}