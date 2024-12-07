package eu.wedgess.piholecontrol.presentation.dashboard.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.piholecontrol.presentation.dashboard.model.LineChartInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun ClientQueriesOverTimeSection(
    overTimeData: List<LineChartInfo.ClientsOverLineChart>
) {
    Card(
        modifier = Modifier.padding(horizontal = PiHoleControlTheme.dimens.padding.itemContent)
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
            Text(
                text = stringResource(R.string.home_title_clients_over_time),
                style = MaterialTheme.typography.titleMedium
            )
            ClientQueriesOvertimeGraph(clientsOvertimeChartInfo = overTimeData)
        }
    }
}