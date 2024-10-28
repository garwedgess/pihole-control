package eu.wedgess.mihole.ui.dashboard.view.components

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
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.responses.PiHoleClientsOverTimeData
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun ClientQueriesOverTimeSection(
    overTimeData: PiHoleClientsOverTimeData
) {
    Card(
        modifier = Modifier.padding(horizontal = MiHoleTheme.dimens.padding.itemContent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MiHoleTheme.dimens.padding.itemContent),
            verticalArrangement = Arrangement.spacedBy(
                MiHoleTheme.dimens.padding.itemContentSmall,
                Alignment.CenterVertically
            )
        ) {
            Text(
                text = stringResource(R.string.home_title_clients_over_time),
                style = MaterialTheme.typography.titleMedium
            )
            ClientQueriesOvertimeGraph(overTimeData = overTimeData)
        }
    }
}