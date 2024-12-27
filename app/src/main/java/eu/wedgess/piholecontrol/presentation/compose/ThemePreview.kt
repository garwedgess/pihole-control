package eu.wedgess.piholecontrol.presentation.compose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Theme",
    uiMode = UI_MODE_NIGHT_NO,
    group = "Light Theme",
    showBackground = true
)
@Preview(
    name = "Dark Theme",
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark Theme",
    showBackground = true
)
annotation class ThemePreviewWithBackground

@Preview(
    name = "Light Theme",
    uiMode = UI_MODE_NIGHT_NO,
    group = "Light Theme"
)
@Preview(
    name = "Dark Theme",
    uiMode = UI_MODE_NIGHT_YES,
    group = "Dark Theme"
)
annotation class ThemePreview
