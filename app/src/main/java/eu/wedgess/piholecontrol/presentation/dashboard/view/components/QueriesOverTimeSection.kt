package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jakewharton.threetenabp.AndroidThreeTen
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.presentation.common.components.SectionTitle
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.dashboard.model.OverTimeLineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun QueriesOverTimeSection(overTimeData: OverTimeLineChartInfo) {
    Card(
        modifier = Modifier.padding(horizontal = PiHoleControlTheme.dimens.padding.screenContent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PiHoleControlTheme.dimens.padding.itemContent),
            verticalArrangement = Arrangement.spacedBy(
                PiHoleControlTheme.dimens.padding.itemContentSmall,
                Alignment.CenterVertically
            )
        ) {
            SectionTitle.TitleOnly(title = stringResource(R.string.home_title_queries_over_time))
            QueriesOvertimeGraph(overTimeData = overTimeData)
        }
    }
}

@ThemePreview
@Composable
private fun QueriesOverTimeSectionPreview() {
    AndroidThreeTen.init(LocalContext.current)
    PiHoleControlTheme {
        Surface {
            QueriesOverTimeSection(
                overTimeData = OverTimeLineChartInfo(
                    permittedChartInfo = LineChartInfo.PermittedQueriesOverLineChart(
                        entity = listOf(
                            OverTimeEntity(timestamp = 1734367500, hits = 267),
                            OverTimeEntity(timestamp = 1734368100, hits = 296),
                            OverTimeEntity(timestamp = 1734368700, hits = 250),
                            OverTimeEntity(timestamp = 1734369300, hits = 100),
                            OverTimeEntity(timestamp = 1734369900, hits = 192),
                            OverTimeEntity(timestamp = 1734370500, hits = 192)
                        )
                    ),
                    blockedChartInfo = LineChartInfo.BlockedQueriesOverLineChart(
                        entity = listOf(
                            OverTimeEntity(timestamp = 1734367500, hits = 127),
                            OverTimeEntity(timestamp = 1734368100, hits = 156),
                            OverTimeEntity(timestamp = 1734368700, hits = 25),
                            OverTimeEntity(timestamp = 1734369300, hits = 0),
                            OverTimeEntity(timestamp = 1734369900, hits = 92),
                            OverTimeEntity(timestamp = 1734370500, hits = 56)
                        )
                    )
                )
            )
        }
    }
}
