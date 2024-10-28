package eu.wedgess.mihole.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(modifier: Modifier, errorType: ResultType.Error) {
    when (errorType) {
        is ResultType.Error.WithTitle -> ErrorContent(
            title = errorType.title.asString(),
            modifier = modifier
        )
        is ResultType.Error.WithTitleAndSubTitle -> ErrorContent(
            title = errorType.title.asString(),
            subTitle = errorType.subTitle.asString(),
            modifier = modifier
        )
    }
}