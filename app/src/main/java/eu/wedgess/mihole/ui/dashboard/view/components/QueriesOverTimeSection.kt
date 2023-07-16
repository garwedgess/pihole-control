package eu.wedgess.mihole.ui.dashboard.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.data.model.PiHoleOverTimeData
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun QueriesOverTimeSection(
    overTimeData: PiHoleOverTimeData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MiHoleTheme.dimens.padding.itemContentXLarge),
        verticalArrangement = Arrangement.spacedBy(
            MiHoleTheme.dimens.padding.itemContentSmall,
            Alignment.CenterVertically
        )
    ) {
        Text(
            modifier = Modifier.padding(start = MiHoleTheme.dimens.padding.itemContent),
            text = stringResource(R.string.home_title_queries_over_time),
            style = MaterialTheme.typography.titleLarge
        )
        QueriesOvertimeGraph(overTimeData = overTimeData)
    }
}