package eu.wedgess.piholecontrol.presentation.compose

import androidx.compose.runtime.Composable

sealed class UIResult<out T> {
    data class Empty(val emptyType: ResultType.Empty) : UIResult<Nothing>()
    data class Loading(val loadingType: ResultType.Loading) : UIResult<Nothing>()
    data class Loaded<T>(val data: T) : UIResult<T>()
    data class Error(val errorType: ResultType.Error) : UIResult<Nothing>()
}

@Composable
fun <T> UIResult<T>.Compose(
    onLoaded: @Composable (T) -> Unit,
    onLoading: (@Composable (ResultType.Loading) -> Unit)? = null,
    onError: (@Composable (ResultType.Error) -> Unit)? = null,
    onEmpty: (@Composable (ResultType.Empty) -> Unit)? = null
) {
    when (this) {
        is UIResult.Loaded -> onLoaded(this.data)
        is UIResult.Error -> onError?.invoke(this.errorType)
        is UIResult.Loading -> onLoading?.invoke(this.loadingType)
        is UIResult.Empty -> onEmpty?.invoke(this.emptyType)
    }
}
