package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.extensions.formatPercentage
import eu.wedgess.mihole.utils.extensions.formatWithThousands

object SummaryItem {

    @Composable
    operator fun invoke(
        title: String,
        value: String,
        caption: String?,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MiHoleTheme.dimens.padding.screenContent),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier.size(MiHoleTheme.dimens.size.summaryIcon),
                    contentDescription = title,
                    tint = Color.Black.copy(alpha = MiHoleTheme.dimens.weight.minAlpha)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text(
                            text = title,
                            color = Color.White.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                       caption?.run {
                            Text(
                                modifier = Modifier.padding(
                                    start = MiHoleTheme.dimens.padding.itemContentSmall
                                ),
                                text = stringResource(id = R.string.home_caption_clients, caption),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                maxLines = 1,
                                color = Color.White.copy(alpha = 0.6f),
                                overflow = TextOverflow.Ellipsis
                            )
                       }
                    }
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
        SummaryItem(title, value.formatPercentage(), caption = null, imageVector, backgroundColor, modifier)
    }

    @Composable
    fun Number(
        title: String,
        value: Int,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        SummaryItem(title, value.formatWithThousands(), caption = null, imageVector, backgroundColor, modifier)
    }

    @Composable
    fun NumberWithCaption(
        title: String,
        value: Int,
        caption: String,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        SummaryItem(title, value.formatWithThousands(), caption = caption, imageVector, backgroundColor, modifier)
    }
}


@Preview
@Composable
fun SummaryItemPreview() {
    MiHoleTheme {

    }
}