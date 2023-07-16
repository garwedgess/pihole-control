package eu.wedgess.mihole.ui.base

import eu.wedgess.mihole.utils.UiText

sealed class UiResult<out T> {
    data class Success<T>(val data: T): UiResult<T>()
    data class Error(val errorMessage: UiText): UiResult<Nothing>()
    object Loading: UiResult<Nothing>()
}
