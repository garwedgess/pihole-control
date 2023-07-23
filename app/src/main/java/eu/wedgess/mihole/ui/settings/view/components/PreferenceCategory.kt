package eu.wedgess.mihole.ui.settings.view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun PreferenceCategory(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        modifier = modifier
            .padding(
                start = MiHoleTheme.dimens.padding.itemContentLarge,
                top = MiHoleTheme.dimens.padding.itemContentXLarge,
                end = MiHoleTheme.dimens.padding.itemContentLarge,
                bottom = MiHoleTheme.dimens.padding.itemContent,
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