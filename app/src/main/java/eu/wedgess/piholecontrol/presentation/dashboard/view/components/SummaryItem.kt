package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
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
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground
import eu.wedgess.piholecontrol.utils.extensions.formatPercentage
import eu.wedgess.piholecontrol.utils.extensions.formatWithThousands

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
                    .padding(PiHoleControlTheme.dimens.padding.itemContentSmall),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = imageVector,
                    modifier = Modifier.size(PiHoleControlTheme.dimens.size.summaryIcon),
                    contentDescription = title,
                    tint = Color.Black.copy(alpha = PiHoleControlTheme.dimens.weight.minAlpha)
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = title,
                            color = Color.White.copy(
                                alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                            ),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        caption?.run {
                            Text(
                                modifier = Modifier.padding(
                                    start = PiHoleControlTheme.dimens.padding.itemContentXSmall
                                ),
                                text = this@run,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                maxLines = 1,
                                color = Color.White.copy(
                                    alpha = PiHoleControlTheme.dimens.weight.tertiaryTextAlpha
                                ),
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
        SummaryItem(
            title = title,
            value = value.formatPercentage(),
            caption = null,
            imageVector = imageVector,
            backgroundColor = backgroundColor,
            modifier = modifier
        )
    }

    @Composable
    fun Number(
        title: String,
        value: Int,
        imageVector: ImageVector,
        backgroundColor: Color,
        modifier: Modifier = Modifier
    ) {
        SummaryItem(
            title,
            value.formatWithThousands(),
            caption = null,
            imageVector,
            backgroundColor,
            modifier
        )
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
        SummaryItem(
            title,
            value.formatWithThousands(),
            caption = caption,
            imageVector,
            backgroundColor,
            modifier
        )
    }
}

@Preview
@Composable
private fun SummaryItemPreview() {
    PiHoleControlTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryItem.Percentage(
                title = "Percentage",
                value = 80.4f,
                imageVector = Icons.Default.PieChart,
                backgroundColor = MaterialTheme.colorScheme.percentageBlockedBackground
            )
            SummaryItem.Number(
                title = "Number",
                value = 80000,
                imageVector = Icons.Default.BackHand,
                backgroundColor = MaterialTheme.colorScheme.domainsOnAdListBackground
            )
            SummaryItem.NumberWithCaption(
                title = "Number",
                caption = stringResource(id = R.string.home_caption_clients, 123),
                value = 80000,
                imageVector = Icons.Default.Public,
                backgroundColor = MaterialTheme.colorScheme.totalQueriesBackground
            )
        }
    }
}
