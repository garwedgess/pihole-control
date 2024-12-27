package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.SummaryEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.percentageBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun SummarySection(summary: SummaryEntity) {
    val totalQueries by animateIntAsState(
        targetValue = summary.dnsQueries,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "total queries animation"
    )

    val adsBlocked by animateIntAsState(
        targetValue = summary.adsBlocked,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "ads blocked animation"
    )

    val domainsBeingBlocked by animateIntAsState(
        targetValue = summary.domainsBlocked,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "domains blocked animation"
    )

    val percentageBlocked by animateFloatAsState(
        targetValue = summary.adsPercentage,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "percentage blocked animation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PiHoleControlTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            )
        ) {
            SummaryItem.NumberWithCaption(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.summary_title_total_queries),
                value = totalQueries,
                caption = stringResource(id = R.string.home_caption_clients, summary.uniqueClients),
                imageVector = Icons.Default.Public,
                backgroundColor = MaterialTheme.colorScheme.totalQueriesBackground
            )
            SummaryItem.Number(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.summary_title_blocked_queries),
                value = adsBlocked,
                imageVector = Icons.Default.BackHand,
                backgroundColor = MaterialTheme.colorScheme.queriesBlockedBackground
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent
            )
        ) {
            SummaryItem.Percentage(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.summary_title_percent_blocked),
                value = percentageBlocked,
                imageVector = Icons.Default.PieChart,
                backgroundColor = MaterialTheme.colorScheme.percentageBlockedBackground
            )
            SummaryItem.Number(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.summary_title_domains_on_blocklist),
                value = domainsBeingBlocked,
                imageVector = Icons.AutoMirrored.Filled.ListAlt,
                backgroundColor = MaterialTheme.colorScheme.domainsOnAdListBackground
            )
        }
    }
}

@ThemePreview
@Composable
private fun SummarySectionPreview() {
    PiHoleControlTheme {
        Surface {
            SummarySection(
                summary = SummaryEntity(
                    dnsQueries = 234565,
                    adsBlocked = 2456,
                    domainsBlocked = 1234567,
                    adsPercentage = 30f,
                    uniqueClients = 25
                )
            )
        }
    }
}
