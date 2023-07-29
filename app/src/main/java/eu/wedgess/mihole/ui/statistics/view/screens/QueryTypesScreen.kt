package eu.wedgess.mihole.ui.statistics.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.common.ErrorMessage
import eu.wedgess.mihole.ui.common.LoadingContent
import eu.wedgess.mihole.ui.statistics.view.content.QueryTypesContent

@Composable
fun QueryTypesScreen(statistics: UiResult<PiHoleStatistics>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (statistics) {
            is UiResult.Loading -> LoadingContent(message = stringResource(R.string.statistics_msg_loading_query_type))
            is UiResult.Error -> ErrorMessage(errorMessage = statistics.errorMessage.asString(), onRetry = {})
            is UiResult.Success -> QueryTypesContent(
                queryTypes = statistics.data.queryTypes
            )
        }
    }
}