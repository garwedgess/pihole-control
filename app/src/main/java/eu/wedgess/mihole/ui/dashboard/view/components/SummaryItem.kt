package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.extensions.formatPercentage
import eu.wedgess.mihole.utils.extensions.formatWithThousands

object SummaryItem {

    @Composable
    operator fun invoke(
        title: String,
        value: String,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        Surface(
            modifier = modifier,
            shadowElevation = 4.dp,
            color = backgroundColor,
            shape = RoundedCornerShape(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier.size(52.dp),
                    contentDescription = title,
                    tint = Color.Black.copy(alpha = 0.1f)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = value,
                        color = Color.White,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    @Composable
    fun Percentage(
        title: String,
        value: Float,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        SummaryItem(title, value.formatPercentage(), imageVector, backgroundColor, modifier)
    }

    @Composable
    fun Number(
        title: String,
        value: Int,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        SummaryItem(title, value.formatWithThousands(), imageVector, backgroundColor, modifier)
    }

}


@Preview
@Composable
fun SummaryItemPreview() {
    MiHoleTheme {

    }
}