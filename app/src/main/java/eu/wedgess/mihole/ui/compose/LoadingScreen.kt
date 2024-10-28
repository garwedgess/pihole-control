package eu.wedgess.mihole.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen(modifier: Modifier, loadingType: ResultType.Loading) {
    when (loadingType) {
        is ResultType.Loading.WithTitle -> {
            LoadingContent(
                title = loadingType.title.asString(),
                modifier = modifier
            )
        }
        is ResultType.Loading.WithTitleAndSubTitle -> LoadingContent(
            title = loadingType.title.asString(),
            subTitle = loadingType.subtitle.asString(),
            modifier = modifier
        )
    }
}