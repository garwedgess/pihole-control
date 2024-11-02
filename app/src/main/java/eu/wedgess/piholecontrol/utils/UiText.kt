package eu.wedgess.piholecontrol.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(
        val value: String
    ) : UiText()

    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any>? = null
    ) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> args?.run { context.getString(id, *this.toTypedArray()) }
                ?: context.getString(id)
        }
    }

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> args?.run {
                stringResource(
                    id = id,
                    formatArgs = this.toTypedArray()
                )
            } ?: stringResource(id = id)
        }
    }
}