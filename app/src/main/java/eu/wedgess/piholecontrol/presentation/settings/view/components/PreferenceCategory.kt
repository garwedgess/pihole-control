package eu.wedgess.piholecontrol.presentation.settings.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun PreferenceCategory(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier
            .padding(
                start = PiHoleControlTheme.dimens.padding.itemContentLarge,
                top = PiHoleControlTheme.dimens.padding.itemContentXLarge,
                end = PiHoleControlTheme.dimens.padding.itemContentLarge,
                bottom = PiHoleControlTheme.dimens.padding.itemContent,
            ),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun PreferenceCategoryPreview() {
    PreferenceCategory(title = "Miscellaneous")
}