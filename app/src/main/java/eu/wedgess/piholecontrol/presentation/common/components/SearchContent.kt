package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    placeHolderText: String,
    searchQuery: String,
    showSearchView: Boolean,
    onSearch: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onClearSearchQuery: (String) -> Unit,
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                onSearch = onSearch,
                query = searchQuery,
                onQueryChange = onQueryChange,
                expanded = showSearchView,
                onExpandedChange = onExpandedChange,
                placeholder = { Text(placeHolderText) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onClearSearchQuery(searchQuery) }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
            )
        },
        expanded = showSearchView,
        onExpandedChange = onExpandedChange,
        content = {}
    )
}
