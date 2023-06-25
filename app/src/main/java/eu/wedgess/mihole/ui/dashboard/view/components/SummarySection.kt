package eu.wedgess.mihole.ui.dashboard.view.components

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
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Public
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.PiHoleSummary
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun SummarySection(summary: PiHoleSummary) {

    val totalQueries by animateIntAsState(
        targetValue = summary.dnsQueriesToday,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    val adsBlocked by animateIntAsState(
        targetValue = summary.adsBlockedToday,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    val domainsBeingBlocked by animateIntAsState(
        targetValue = summary.domainsBeingBlocked,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    val percentageBlocked by animateFloatAsState(
        targetValue = summary.adsPercentageToday,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryItem.Number(
                modifier = Modifier.weight(1f),
                title = "Total Queries",
                value = totalQueries,
                imageVector = Icons.Default.Public,
                backgroundColor = Color(0xFF00A65A)
            )
            SummaryItem.Number(
                modifier = Modifier.weight(1f),
                title = "Blocked Queries",
                value = adsBlocked,
                imageVector = Icons.Default.BackHand,
                backgroundColor = Color(0xFF00C0EF)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryItem.Percentage(
                modifier = Modifier.weight(1f),
                title = "Percent Blocked",
                value = percentageBlocked,
                imageVector = Icons.Default.PieChart,
                backgroundColor = Color(0xFFF39C12)
            )
            SummaryItem.Number(
                modifier = Modifier.weight(1f),
                title = "Domains on Blocklist",
                value = domainsBeingBlocked,
                imageVector = Icons.Default.ListAlt,
                backgroundColor = Color(0xFFDD4B39)
            )
        }
    }
}

@Preview
@Composable
private fun SummarySectionPreview() {
    MiHoleTheme {

    }
}