package eu.wedgess.mihole.ui.common.search

import androidx.compose.ui.text.input.TextFieldValue

data class SearchState<out T>(
    val initialResults: List<T> = emptyList(),
    val searchResults: List<T> = emptyList(),
    val query: TextFieldValue = TextFieldValue(),
    val searching: Boolean = false
) {
    private var previousQueryText: String = ""

    fun sameAsPreviousQuery() = query.text == previousQueryText

    fun clearPreviousQueryText() {
        previousQueryText = ""
    }

    val searchDisplay: SearchStatus
        get() = when {
            query.text.isEmpty() -> SearchStatus.InitialResults
            searching -> SearchStatus.SearchInProgress
            searchResults.isEmpty() -> {
                previousQueryText = query.text
                SearchStatus.NoResults
            }
            else -> {
                previousQueryText = query.text
                SearchStatus.Results
            }
        }

}

enum class SearchStatus {
    InitialResults,
    SearchInProgress,
    Results,
    NoResults
}