package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.ui.dashboard.model.LineChartData
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
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
            label = "Blocked",
            data = overTimeData.adsOverTime.map { Pair(it.key * 1000L, it.value) },
            color = adsColor
        )
    }

    val domainsColor = MaterialTheme.colorScheme.totalQueriesBackground
    val domainsEntry = remember(overTimeData.domainsOverTime) {
        LineChartData(
            label = "Permitted",
            data = overTimeData.domainsOverTime.map { Pair(it.key * 1000L, it.value) },
            color = domainsColor
        )
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize(),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            LineChart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp),
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
                        title = "Permitted",
                        subTitle = "Hits: ${domainsEntry.data.sumOf { it.second.toInt() }.formatWithThousands()}",
                        color = domainsColor
                    ),
                    LegendData(
                        title = "Blocked",
                        subTitle = "Hits: ${adsEntry.data.sumOf { it.second.toInt() }.formatWithThousands()}",
                        color = adsColor
                    )
                )
            )
        }
    }
}