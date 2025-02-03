package eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.presentation.common.components.SectionTitle
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.statistics.common.components.StatisticsListItem
import eu.wedgess.piholecontrol.presentation.statistics.tabs.topdomains.model.TopDomainsInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground

@Composable
fun TopDomainsContent(
    topPermittedDomains: TopDomainsInfo,
    topBlockedDomains: TopDomainsInfo
) {
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
            Column(modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.itemContent)) {
                SectionTitle.TitleWithIcon(
                    title = stringResource(R.string.statistics_title_top_permitted),
                    icon = Icons.Default.GppGood,
                    iconTint = MaterialTheme.colorScheme.totalQueriesBackground
                )
                Spacer(
                    modifier = Modifier.height(
                        PiHoleControlTheme.dimens.padding.itemContentSmall
                    )
                )
                topPermittedDomains.topDomains.forEachIndexed { index, (permittedDomain, hits, percentage) ->
                    StatisticsListItem(
                        domain = permittedDomain,
                        hits = hits,
                        progress = percentage
                    )
                    if (index < topPermittedDomains.topDomains.size - 1) {
                        Spacer(
                            modifier = Modifier.height(
                                PiHoleControlTheme.dimens.padding.screenContent
                            )
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = topBlockedDomains.topDomains.isNotEmpty()) {
            Card {
                Column(modifier = Modifier.padding(PiHoleControlTheme.dimens.padding.itemContent)) {
                    SectionTitle.TitleWithIcon(
                        title = stringResource(R.string.statistics_title_top_blocked),
                        icon = Icons.Default.GppBad,
                        iconTint = MaterialTheme.colorScheme.domainsOnAdListBackground
                    )
                    Spacer(
                        modifier = Modifier.height(
                            PiHoleControlTheme.dimens.padding.itemContentSmall
                        )
                    )
                    topBlockedDomains.topDomains.forEachIndexed { index, (blockedDomain, hits, percentage) ->
                        StatisticsListItem(
                            domain = blockedDomain,
                            hits = hits,
                            progress = percentage
                        )
                        if (index < topBlockedDomains.topDomains.size - 1) {
                            Spacer(
                                modifier = Modifier.height(
                                    PiHoleControlTheme.dimens.padding.screenContent
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@ThemePreview
@Composable
private fun TopDomainsContentPreview() {
    PiHoleControlTheme {
        Surface {
            TopDomainsContent(
                topPermittedDomains = TopDomainsInfo(
                    listOf(
                        TopDomainEntity("www.google.com", 5765, 10f),
                        TopDomainEntity("www.google.com", 3456, 10f),
                        TopDomainEntity("www.google.com", 3213, 10f),
                        TopDomainEntity("www.google.com", 2435, 10f),
                        TopDomainEntity("www.google.com", 2345, 10f),
                        TopDomainEntity("www.google.com", 1890, 10f),
                        TopDomainEntity("www.google.com", 1678, 10f),
                        TopDomainEntity("www.google.com", 900, 10f),
                        TopDomainEntity("www.google.com", 500, 10f),
                        TopDomainEntity("www.google.com", 165, 10f)
                    )
                ),
                topBlockedDomains = TopDomainsInfo(
                    topDomains = listOf(
                        TopDomainEntity("www.google.com", 5765, 10f),
                        TopDomainEntity("www.google.com", 3456, 10f),
                        TopDomainEntity("www.google.com", 3213, 10f),
                        TopDomainEntity("www.google.com", 2435, 10f),
                        TopDomainEntity("www.google.com", 2345, 10f),
                        TopDomainEntity("www.google.com", 1890, 10f),
                        TopDomainEntity("www.google.com", 1678, 10f),
                        TopDomainEntity("www.google.com", 900, 10f),
                        TopDomainEntity("www.google.com", 500, 10f),
                        TopDomainEntity("www.google.com", 165, 10f)
                    )
                )
            )
        }
    }
}
