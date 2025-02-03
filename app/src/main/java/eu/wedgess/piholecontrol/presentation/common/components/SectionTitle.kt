package eu.wedgess.piholecontrol.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

object SectionTitle {

    @Composable
    operator fun invoke(
        title: String,
        icon: ImageVector? = null,
        iconTint: Color = Color.Unspecified
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentLarge,
                Alignment.Start
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.run {
                Icon(
                    modifier = Modifier.size(
                        PiHoleControlTheme.dimens.size.statisticsTitleIcon
                    ),
                    tint = iconTint,
                    imageVector = icon,
                    contentDescription = title
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    @Composable
    fun TitleOnly(title: String) = SectionTitle(title = title)

    @Composable
    fun TitleWithIcon(title: String, icon: ImageVector, iconTint: Color) {
        SectionTitle(title = title, icon = icon, iconTint = iconTint)
    }
}
