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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    placeHolderText: String,
    searchQuery: String,
    showSearchView: Boolean,
    onQueryChange: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onClearSearchQuery: (String) -> Unit,
    onSearch: ((String) -> Unit)? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val localKeyboardController = LocalSoftwareKeyboardController.current

    SearchBar(
        modifier = Modifier.focusRequester(focusRequester),
        inputField = {
            SearchBarDefaults.InputField(
                onSearch = {
                    focusRequester.freeFocus()
                    localKeyboardController?.hide()
                    onSearch?.invoke(it)
                },
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

    LaunchedEffect(key1 = showSearchView) {
        if (showSearchView) {
            delay(100)
            focusRequester.requestFocus()
            localKeyboardController?.show()
        }
    }
}
