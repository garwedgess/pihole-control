package eu.wedgess.piholecontrol.presentation.settings.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun RegularPreference(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    subtitle: String? = null,
    enabled: Boolean = true,
) {
    RegularPreferenceImpl(
        title = title,
        subtitleAnnotatedString = subtitle?.run { AnnotatedString(text = this) },
        icon = icon,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
private fun RegularPreferenceImpl(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    subtitleAnnotatedString: AnnotatedString? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick,
            )
            .padding(all = PiHoleControlTheme.dimens.padding.itemContentLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.run {
            Icon(
                modifier = Modifier.padding(end = PiHoleControlTheme.dimens.padding.itemContentLarge),
                imageVector = icon,
                contentDescription = title
            )
        }
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = if (enabled) {
                    Color.Unspecified
                } else MaterialTheme.colorScheme.onSurface.copy(alpha = PiHoleControlTheme.dimens.weight.disabledTextAlpha),
            )

            subtitleAnnotatedString?.let { subtitle ->
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = PiHoleControlTheme.dimens.weight.disabledTextAlpha)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegularPreferencePreview() {
    RegularPreference(
        title = "Advanced settings",
        subtitle = "Lorem ipsum dolor sit amet",
        icon = Icons.Default.Palette,
        onClick = { },
    )
}