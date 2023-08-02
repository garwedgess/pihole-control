package eu.wedgess.mihole.ui.statistics.view.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.common.previews.ThemePreview
import eu.wedgess.mihole.ui.statistics.view.common.StatisticsListItem
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground

@Composable
fun TopDomainsContent(
    topPermittedDomains: List<Pair<String, Int>>,
    topBlockedDomains: List<Pair<String, Int>>
) {

    val sumAllPermitted by remember {
        mutableIntStateOf(topPermittedDomains.sumOf { it.second })
    }

    val sumAllBlocked by remember {
        mutableIntStateOf(topBlockedDomains.sumOf { it.second })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MiHoleTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent)
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MiHoleTheme.dimens.padding.itemContent),
                verticalArrangement = Arrangement.spacedBy(
                    MiHoleTheme.dimens.padding.itemContentXLarge,
                    Alignment.CenterVertically
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContentLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(MiHoleTheme.dimens.size.statisticsTitleIcon),
                        tint = MaterialTheme.colorScheme.totalQueriesBackground,
                        imageVector = Icons.Default.GppGood,
                        contentDescription = "icon"
                    )
                    Text(
                        text = stringResource(id = R.string.statistics_title_top_permitted),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                topPermittedDomains.forEach { permittedDomain ->
                    StatisticsListItem(
                        domain = permittedDomain.first,
                        hits = permittedDomain.second,
                        progress = permittedDomain.second.toFloat().div(sumAllPermitted.toFloat())
                    )
                }
            }
        }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MiHoleTheme.dimens.padding.itemContent),
                verticalArrangement = Arrangement.spacedBy(
                    MiHoleTheme.dimens.padding.itemContentXLarge,
                    Alignment.CenterVertically
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContentLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(MiHoleTheme.dimens.size.statisticsTitleIcon),
                        tint = MaterialTheme.colorScheme.domainsOnAdListBackground,
                        imageVector = Icons.Default.GppBad,
                        contentDescription = "icon"
                    )
                    Text(
                        text = stringResource(id = R.string.statistics_title_top_blocked),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                topBlockedDomains.forEach { blockedDomain ->
                    StatisticsListItem(
                        domain = blockedDomain.first,
                        hits = blockedDomain.second,
                        progress = blockedDomain.second.toFloat().div(sumAllBlocked.toFloat())
                    )
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun TopDomainsContentPreview() {
    MiHoleTheme {
        Surface {
            TopDomainsContent(
                topPermittedDomains = listOf(
                    "www.google.com" to 5765,
                    "www.google.com" to 3456,
                    "www.google.com" to 3213,
                    "www.google.com" to 2435,
                    "www.google.com" to 2345,
                    "www.google.com" to 1890,
                    "www.google.com" to 1678,
                    "www.google.com" to 900,
                    "www.google.com" to 500,
                    "www.google.com" to 165
                ),
                topBlockedDomains = listOf(
                    "www.google.com" to 5765,
                    "www.google.com" to 3456,
                    "www.google.com" to 3213,
                    "www.google.com" to 2435,
                    "www.google.com" to 2345,
                    "www.google.com" to 1890,
                    "www.google.com" to 1678,
                    "www.google.com" to 900,
                    "www.google.com" to 500,
                    "www.google.com" to 165
                )
            )
        }
    }
}