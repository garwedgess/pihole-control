package eu.wedgess.mihole.ui.statistics.view

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
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.domainsOnAdListBackground
import eu.wedgess.mihole.ui.theme.queriesBlockedBackground
import eu.wedgess.mihole.ui.theme.totalQueriesBackground
import eu.wedgess.mihole.utils.extensions.formatWithThousands
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun TopDomainsContent(
    topPermittedDomains: List<Pair<String, Int>>,
    topBlockedDomains: List<Pair<String, Int>>
) {

    val sumAllPermitted = remember {
        mutableStateOf(topPermittedDomains.sumOf { it.second })
    }

    val sumAllBlocked = remember {
        mutableStateOf(topBlockedDomains.sumOf { it.second })
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), state = rememberLazyListState()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.totalQueriesBackground,
                    imageVector = Icons.Default.GppGood,
                    contentDescription = "icon"
                )
                Text(text = "Top permitted", style = MaterialTheme.typography.titleLarge)
            }
        }
        items(topPermittedDomains) { permittedDomain ->
            val progress = remember {
                permittedDomain.second.toFloat().div(sumAllPermitted.value.toFloat())
            }
            val animatedProgress = animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            ).value
            val count by animateIntAsState(
                targetValue = permittedDomain.second,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
            ListItem(
                headlineContent = {
                    Text(text = permittedDomain.first)
                },
                supportingContent = {
                    Text(text = count.formatWithThousands())
                },
                trailingContent = {
                    LinearProgressIndicator(
                        modifier = Modifier.widthIn(max = 100.dp),
                        progress = animatedProgress
                    )
                }
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.domainsOnAdListBackground,
                    imageVector = Icons.Default.GppBad,
                    contentDescription = "icon"
                )
                Text(text = "Top blocked", style = MaterialTheme.typography.titleLarge)
            }
        }
        items(topBlockedDomains) { blockedDomain ->
            val progress = remember {
                blockedDomain.second.toFloat().div(sumAllBlocked.value.toFloat())
            }
            val animatedProgress = animateFloatAsState(
                targetValue = progress,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            ).value
            val count by animateIntAsState(
                targetValue = blockedDomain.second,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = FastOutSlowInEasing
                )
            )
            ListItem(
                headlineContent = {
                    Text(text = blockedDomain.first)
                },
                supportingContent = {
                    Text(text = count.formatWithThousands())
                },
                trailingContent = {
                    LinearProgressIndicator(
                        modifier = Modifier.widthIn(max = 100.dp),
                        progress = animatedProgress
                    )
                }
            )
        }
    }
}