package eu.wedgess.mihole.ui.common.search

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun <T> SearchContent(
    modifier: Modifier = Modifier,
    state: SearchState<T>,
    onQueryChanged: (query: TextFieldValue) -> Unit,
    onClosed: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        val dispatcher: OnBackPressedDispatcher =
            requireNotNull(LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher)

        val backCallback = remember {
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = false
                    onQueryChanged(TextFieldValue(""))
                    keyboardController?.hide()
                    onClosed()
                }
            }
        }

        DisposableEffect(dispatcher) { // dispose/relaunch if dispatcher changes
            dispatcher.addCallback(backCallback)
            onDispose {
                backCallback.remove() // avoid leaks!
            }
        }

        SearchBar(
            query = state.query,
            onQueryChange = onQueryChanged,
            onClearQuery = { onQueryChanged(TextFieldValue("")) },
            onClose = onClosed,
            searching = state.searching,
            modifier = modifier
        )
    }
}