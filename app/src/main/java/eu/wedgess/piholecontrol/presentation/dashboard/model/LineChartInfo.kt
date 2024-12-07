package eu.wedgess.piholecontrol.presentation.dashboard.model

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.domain.model.ClientOverTimeEntity
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.presentation.theme.domainsOnAdListBackground
import eu.wedgess.piholecontrol.presentation.theme.totalQueriesBackground
import eu.wedgess.piholecontrol.utils.ColorGenerator
import eu.wedgess.piholecontrol.utils.UiText
import java.text.DateFormat

sealed class LineChartInfo(
    val title: UiText,
    val legendSubTitle: UiText,
    val entries: Iterable<LineChartEntry>
) {

    @Composable
    open fun color(): Color = Color.Unspecified

    data class PermittedQueriesOverLineChart(
        val entity: List<OverTimeEntity>
    ) : LineChartInfo(
        title = UiText.StringResource(R.string.home_title_queries_over_time_permitted),
        legendSubTitle = UiText.StringResource(
            R.string.home_legend_sub_title_queries_over_time,
            listOf(entity.sumOf { it.hits })
        ),
        entries = entity.mapIndexed { index, item ->
            LineChartEntry(
                x = index.toFloat(),
                y = item.hits.toFloat(),
                xDisplayValue = item.time,
                yLabel = "Permitted"
            )
        }
    ) {
        @Composable
        override fun color(): Color = MaterialTheme.colorScheme.totalQueriesBackground
    }

    data class BlockedQueriesOverLineChart(
        val entity: List<OverTimeEntity>
    ) : LineChartInfo(
        title = UiText.StringResource(R.string.home_title_queries_over_time_blocked),
        legendSubTitle = UiText.StringResource(
            R.string.home_legend_sub_title_queries_over_time,
            listOf(entity.sumOf { it.hits })
        ),
        entries = entity.mapIndexed { index, item ->
            LineChartEntry(
                x = index.toFloat(),
                y = item.hits.toFloat(),
                xDisplayValue = item.time,
                yLabel = "Blocked"
            )
        }
    ) {
        @Composable
        override fun color(): Color = MaterialTheme.colorScheme.domainsOnAdListBackground
    }

    data class ClientsOverLineChart(
        val entity: ClientOverTimeEntity,
        val formatter: DateFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
    ) : LineChartInfo(
        title = UiText.DynamicString(entity.clientName.takeIf { it.isNotBlank() }
            ?: entity.clientIp),
        legendSubTitle = UiText.StringResource(
            R.string.home_legend_sub_title_queries_over_time,
            listOf(entity.clientActivity.sumOf { it.hits })
        ),
        entries = entity.clientActivity.mapIndexed { index, item ->
            LineChartEntry(
                x = index.toFloat(),
                y = item.hits.toFloat(),
                xDisplayValue = formatter.format(item.timestamp),
                yLabel = entity.clientName.takeIf { it.isNotBlank() } ?: entity.clientIp
            )
        }
    ) {
        @Composable
        override fun color(): Color =
            ColorGenerator(!isSystemInDarkTheme()).generateColor(entity.clientName + entity.clientIp)
    }
}