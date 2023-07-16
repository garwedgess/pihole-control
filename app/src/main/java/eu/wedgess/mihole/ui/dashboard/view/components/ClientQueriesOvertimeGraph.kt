package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleClientsOverTimeData
import eu.wedgess.mihole.ui.common.LegendData
import eu.wedgess.mihole.ui.common.LegendGridImpl
import eu.wedgess.mihole.ui.dashboard.model.LineChartData
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.ColorGenerator
import eu.wedgess.mihole.utils.UiText
import eu.wedgess.mihole.utils.extensions.formatWithThousands
import java.text.DateFormat

@Composable
fun ClientQueriesOvertimeGraph(
    overTimeData: PiHoleClientsOverTimeData
) {

    val darkTheme = isSystemInDarkTheme()
    val colorGenerator = remember {
        ColorGenerator(isLightTheme = !darkTheme)
    }
    val dateFormatter = remember {
        DateFormat.getTimeInstance(DateFormat.SHORT)
    }

    val clientsEntry = remember(overTimeData.clientsOverTime) {

        val clientDataOverTime = overTimeData.clients
            .mapIndexed { index, client ->
                client to overTimeData.clientsOverTime.map { (k, v) ->
                    k to v[index]
                }
            }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.flatten() }

        clientDataOverTime.map { (client, clientActivityList) ->
            LineChartData(
                label = UiText.DynamicString(client.name.takeIf { it.isNotBlank() } ?: client.ip),
                data = clientActivityList.map {
                    Pair(it.first * 1000L, it.second.toFloat())
                },
                color = colorGenerator.generateColor(client.name + client.ip)
            )
        }.sortedByDescending { chartData -> chartData.data.sumOf { it.second.toInt() } }
    }

    Card(
        modifier = Modifier.padding(horizontal = MiHoleTheme.dimens.padding.itemContent)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MiHoleTheme.dimens.padding.itemContent)
        ) {
            LineChart(
                modifier = Modifier.padding(vertical = MiHoleTheme.dimens.padding.screenContent),
                data = clientsEntry,
                xAxisFormatter = dateFormatter.run {
                    { value ->
                        this.format(value)
                    }
                }
            )

            LegendGridImpl(
                legendData = clientsEntry.map { linesChartData ->
                    LegendData(
                        title = linesChartData.label.asString(),
                        subTitle = stringResource(
                            id = R.string.home_legend_sub_title_queries_over_time,
                            linesChartData.data.sumOf { it.second.toInt() }.formatWithThousands()
                        ),
                        color = linesChartData.color ?: Color.Unspecified
                    )
                }
            )
        }
    }
}