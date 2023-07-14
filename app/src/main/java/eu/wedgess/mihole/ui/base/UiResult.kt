package eu.wedgess.mihole.ui.base

sealed class UiResult<out T> {
    data class Success<T>(val data: T): UiResult<T>()
    data class Error(val errorMessage: String): UiResult<Nothing>()
    object Loading: UiResult<Nothing>()
}
