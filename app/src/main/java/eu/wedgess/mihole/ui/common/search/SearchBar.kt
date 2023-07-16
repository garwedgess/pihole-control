package eu.wedgess.mihole.ui.common.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClearQuery: () -> Unit,
    onClose: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Back button
        IconButton(
            modifier = Modifier.padding(start = MiHoleTheme.dimens.padding.searchBarIconPaddingStart),
            onClick = {
                keyboardController?.hide()
                onClose()
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        SearchTextField(
            query,
            onQueryChange,
            onClearQuery,
            searching,
            modifier.weight(MiHoleTheme.dimens.weight.searchTextField)
        )
    }
}

/**
 * This is a stateless TextField for searching with a Hint when query is empty,
 * and clear and loading [IconButton]s to clear query or show progress indicator when
 * a query is in progress.
 */
@Composable
private fun SearchTextField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClearQuery: () -> Unit,
    searching: Boolean,
    modifier: Modifier = Modifier
) {

    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier
            .then(
                Modifier
                    .height(MiHoleTheme.dimens.size.searchBarHeight)
                    .padding(
                        top = MiHoleTheme.dimens.padding.itemContent,
                        bottom = MiHoleTheme.dimens.padding.screenContent,
                        start = MiHoleTheme.dimens.padding.none,
                        end = MiHoleTheme.dimens.padding.itemContentLarge
                    )
            ),
        color = MaterialTheme.colorScheme.inversePrimary,
        shape = RoundedCornerShape(percent = 50),
    ) {

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onPrimaryContainer) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = modifier
            ) {

                if (query.text.isEmpty()) {
                    SearchHint(
                        modifier.padding(
                            start = MiHoleTheme.dimens.padding.itemContentXLarge,
                            end = MiHoleTheme.dimens.padding.itemContent
                        )
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = query,
                        textStyle = LocalTextStyle.current.copy(
                            color = contentColorFor(
                                backgroundColor = MaterialTheme.colorScheme.inversePrimary
                            )
                        ),
                        onValueChange = onQueryChange,
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(MiHoleTheme.dimens.weight.searchTextField)
                            .focusRequester(focusRequester)
                            .padding(
                                top = MiHoleTheme.dimens.padding.itemContent,
                                bottom = MiHoleTheme.dimens.padding.itemContent,
                                start = MiHoleTheme.dimens.padding.itemContentXLarge,
                                end = MiHoleTheme.dimens.padding.itemContent
                            ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        )
                    )

                    when {
                        searching && query.text.isNotEmpty() -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(horizontal = MiHoleTheme.dimens.padding.itemContentSmall)
                                    .size(MiHoleTheme.dimens.size.searchProgress)
                            )
                        }

                        query.text.isNotEmpty() -> {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = null,
                                    tint = contentColorFor(backgroundColor = MaterialTheme.colorScheme.inversePrimary)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchHint(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)

    ) {
        Text(
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.inversePrimary).copy(
                alpha = MiHoleTheme.dimens.weight.searchHintAlpha
            ),
            text = "Search a Tag or Description",
        )
    }
}
