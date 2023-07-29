package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.ui.dashboard.model.LineChartData
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.formatWithThousands
import java.text.DateFormat
import java.text.DateFormat.getTimeInstance

@Composable
fun QueriesOvertimeGraph(
    overTimeData: PiHoleOverTimeData
) {

    val dateFormatter = remember {
        getTimeInstance(DateFormat.SHORT)
    }
    val adsColor = MaterialTheme.colorScheme.domainsOnAdListBackground
    val adsEntry = remember(overTimeData.adsOverTime) {
        LineChartData(
            label = UiText.StringResource(R.string.home_title_queries_over_time_blocked),
            data = overTimeData.adsOverTime.map { Pair(it.key * 1000L, it.value) },
            color = adsColor
        )
    }

    val domainsColor = MaterialTheme.colorScheme.totalQueriesBackground
    val domainsEntry = remember(overTimeData.domainsOverTime) {
        LineChartData(
            label = UiText.StringResource(R.string.home_title_queries_over_time_permitted),
            data = overTimeData.domainsOverTime.map { Pair(it.key * 1000L, it.value) },
            color = domainsColor
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LineChart(
            modifier = Modifier.fillMaxSize().heightIn(min = MiHoleTheme.dimens.size.lineChartHeight),
            data = listOf(adsEntry, domainsEntry),
            xAxisFormatter = dateFormatter.run {
                { value ->
                    this.format(value)
                }
            }
        )
        LegendGridImpl(
            legendData = listOf(
                LegendData(
                    title = stringResource(id = R.string.home_title_queries_over_time_permitted),
                    subTitle = stringResource(
                        id = R.string.home_legend_sub_title_queries_over_time,
                        domainsEntry.data.sumOf { it.second.toInt() }.formatWithThousands()
                    ),
                    color = domainsColor
                ),
                LegendData(
                    title = stringResource(id = R.string.home_title_queries_over_time_blocked),
                    subTitle = stringResource(
                        id = R.string.home_legend_sub_title_queries_over_time,
                        adsEntry.data.sumOf { it.second.toInt() }.formatWithThousands()
                    ),
                    color = adsColor
                )
            )
        )
    }
}