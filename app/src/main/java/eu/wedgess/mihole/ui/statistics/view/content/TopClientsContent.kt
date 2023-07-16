package eu.wedgess.mihole.ui.statistics.view.content

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.ui.theme.queriesBlockedBackground
import eu.wedgess.mihole.utils.extensions.formatWithThousands

@Composable
fun TopClientsContent(
    topClients: List<Pair<String, Int>>
) {

    val sumAllClients = remember {
        mutableIntStateOf(topClients.sumOf { it.second })
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MiHoleTheme.dimens.padding.screenContent),
                horizontalArrangement = Arrangement.spacedBy(MiHoleTheme.dimens.padding.itemContent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(MiHoleTheme.dimens.size.statisticsTitleIcon),
                    tint = MaterialTheme.colorScheme.queriesBlockedBackground,
                    imageVector = Icons.Default.Devices,
                    contentDescription = "icon"
                )
                Text(
                    text = stringResource(R.string.statistics_title_top_clients),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        items(topClients) { client ->
            val progress = remember {
                client.second.toFloat().div(sumAllClients.value.toFloat())
            }
            val animatedProgress = animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            ).value
            val count by animateIntAsState(
                targetValue = client.second,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
            ListItem(
                headlineContent = {
                    Text(text = client.first)
                },
                supportingContent = {
                    Text(text = count.formatWithThousands())
                },
                trailingContent = {
                    LinearProgressIndicator(
                        modifier = Modifier.widthIn(max = MiHoleTheme.dimens.size.listPercentageBarWidth),
                        progress = animatedProgress
                    )
                }
            )
        }
    }
}