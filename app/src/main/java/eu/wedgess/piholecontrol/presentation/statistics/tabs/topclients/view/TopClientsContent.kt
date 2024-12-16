package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.view

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
import androidx.compose.material.icons.filled.Devices
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
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.presentation.common.previews.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view.StatisticsListItem
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground

@Composable
fun TopClientsContent(topClients: List<TopClientEntity>) {
    val sumAllClients by remember {
        mutableIntStateOf(topClients.sumOf { it.hits })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PiHoleControlTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContent)
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PiHoleControlTheme.dimens.padding.screenContent),
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentXLarge,
                    Alignment.CenterVertically
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(PiHoleControlTheme.dimens.padding.itemContentLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(PiHoleControlTheme.dimens.size.statisticsTitleIcon),
                        tint = MaterialTheme.colorScheme.queriesBlockedBackground,
                        imageVector = Icons.Default.Devices,
                        contentDescription = "icon"
                    )
                    Text(
                        text = stringResource(R.string.statistics_title_top_clients),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                topClients.forEach { (client, hits) ->
                    StatisticsListItem(
                        domain = client,
                        hits = hits,
                        progress = hits.toFloat().div(sumAllClients.toFloat())
                    )
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun TopClientsContentPreview() {
    PiHoleControlTheme {
        Surface {
            TopClientsContent(
                topClients = listOf(
                    TopClientEntity("www.google.com", 5765),
                    TopClientEntity("www.google.com", 3456),
                    TopClientEntity("www.google.com", 3213),
                    TopClientEntity("www.google.com", 2435),
                    TopClientEntity("www.google.com", 2345),
                    TopClientEntity("www.google.com", 1890),
                    TopClientEntity("www.google.com", 1678),
                    TopClientEntity("www.google.com", 900),
                    TopClientEntity("www.google.com", 500),
                    TopClientEntity("www.google.com", 165)
                )
            )
        }
    }
}
