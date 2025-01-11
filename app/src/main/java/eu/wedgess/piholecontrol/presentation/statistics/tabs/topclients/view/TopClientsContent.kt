package eu.wedgess.piholecontrol.presentation.statistics.tabs.topclients.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view.StatisticsListItem
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun TopClientsContent(clientQueries: TopClientQueriesEntity) {
    Column(
        modifier = Modifier
            .padding(PiHoleControlTheme.dimens.padding.screenContent)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.screenContent,
            Alignment.CenterVertically
        )
    ) {
        Card {
            Column(
                modifier = Modifier
                    .padding(PiHoleControlTheme.dimens.padding.itemContent),
                verticalArrangement = Arrangement.spacedBy(
                    PiHoleControlTheme.dimens.padding.itemContentSmall
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContentLarge,
                        Alignment.Start
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(
                            PiHoleControlTheme.dimens.size.statisticsTitleIcon
                        ),
                        tint = MaterialTheme.colorScheme.totalQueriesBackground,
                        imageVector = Icons.Default.Devices,
                        contentDescription = "icon"
                    )
                    Text(
                        text = stringResource(id = R.string.statistics_title_top_clients_all),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                clientQueries.all.forEach { (permittedDomain, hits, percentage) ->
                    StatisticsListItem(
                        domain = permittedDomain,
                        hits = hits,
                        progress = percentage
                    )
                }
            }
        }

        AnimatedVisibility(visible = clientQueries.blocked.isNotEmpty()) {
            Card {
                Column(
                    modifier = Modifier
                        .padding(PiHoleControlTheme.dimens.padding.itemContent),
                    verticalArrangement = Arrangement.spacedBy(
                        PiHoleControlTheme.dimens.padding.itemContentSmall
                    )
                ) {
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
                            tint = MaterialTheme.colorScheme.domainsOnAdListBackground,
                            imageVector = Icons.Default.Devices,
                            contentDescription = "icon"
                        )
                        Text(
                            text = stringResource(id = R.string.statistics_title_top_clients_blocked),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    clientQueries.blocked.forEach { (blockedDomain, hits, percentage) ->
                        StatisticsListItem(
                            domain = blockedDomain,
                            hits = hits,
                            progress = percentage
                        )
                    }
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
                clientQueries = TopClientQueriesEntity(
                    all =
                    listOf(
                        TopClientEntity("www.google.com", 5765, 25f),
                        TopClientEntity("www.google1.com", 3456, 15f),
                        TopClientEntity("www.google2.com", 3213, 10f),
                        TopClientEntity("www.google3.com", 2435, 10f),
                        TopClientEntity("www.google4.com", 2345, 10f),
                        TopClientEntity("www.google5.com", 1890, 8f),
                        TopClientEntity("www.google6.com", 1678, 8f),
                        TopClientEntity("www.google7.com", 900, 9.5f),
                        TopClientEntity("www.google8.com", 500, 9.5f),
                        TopClientEntity("www.google9.com", 165, 5f)
                    ),
                    blocked = emptyList()
                )
            )
        }
    }
}
