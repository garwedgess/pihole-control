package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.model.TopClientsInfo
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view.StatisticsListItem
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.queriesBlockedBackground

@Composable
fun TopClientsContent(topClientInfo: TopClientsInfo) {
    Card(
        modifier = Modifier
            .padding(PiHoleControlTheme.dimens.padding.screenContent)
    ) {
        LazyColumn(
            modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.itemContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContent,
                Alignment.CenterVertically
            )
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContentLarge
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(
                            PiHoleControlTheme.dimens.size.statisticsTitleIcon
                        ),
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
            }
            items(topClientInfo.topClients, key = { it.client }) { item ->
                StatisticsListItem(
                    domain = item.client,
                    hits = item.hits,
                    progress = item.hits.toFloat().div(topClientInfo.allHits.toFloat())
                )
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
                topClientInfo = TopClientsInfo(
                    listOf(
                        TopClientEntity("www.google.com", 5765),
                        TopClientEntity("www.google1.com", 3456),
                        TopClientEntity("www.google2.com", 3213),
                        TopClientEntity("www.google3.com", 2435),
                        TopClientEntity("www.google4.com", 2345),
                        TopClientEntity("www.google5.com", 1890),
                        TopClientEntity("www.google6.com", 1678),
                        TopClientEntity("www.google7.com", 900),
                        TopClientEntity("www.google8.com", 500),
                        TopClientEntity("www.google9.com", 165)
                    )
                )
            )
        }
    }
}
