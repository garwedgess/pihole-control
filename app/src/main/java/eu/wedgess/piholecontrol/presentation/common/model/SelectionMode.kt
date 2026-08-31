package eu.wedgess.piholecontrol.presentation.common.model

sealed interface SelectionMode<out T> {
    data object Inactive : SelectionMode<Nothing>

    data class Active<T>(val selected: Set<T>) : SelectionMode<T> {
        val count: Int get() = selected.size
        val hasSelection: Boolean get() = selected.isNotEmpty()
    }
}

fun <T> SelectionMode<T>.isSelected(item: T): Boolean = when (this) {
    is SelectionMode.Active -> item in selected
    SelectionMode.Inactive -> false
}

fun <T> SelectionMode<T>.selectedItems(): Set<T> = when (this) {
    is SelectionMode.Active -> selected
    SelectionMode.Inactive -> emptySet()
}
