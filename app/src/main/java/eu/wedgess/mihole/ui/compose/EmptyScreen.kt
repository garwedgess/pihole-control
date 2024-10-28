package eu.wedgess.mihole.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmptyScreen(modifier: Modifier, emptyType: ResultType.Empty) {
    when (emptyType) {
        is ResultType.Empty.WithTitle -> EmptyContent(
            title = emptyType.title.asString(),
            modifier = modifier
        )

        is ResultType.Empty.WithTitleAndSubTitle -> EmptyContent(
            title = emptyType.title.asString(),
            subTitle = emptyType.subTitle.asString(),
            modifier = modifier
        )
    }
}