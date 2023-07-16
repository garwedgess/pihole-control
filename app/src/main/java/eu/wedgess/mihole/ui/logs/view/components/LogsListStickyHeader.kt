package eu.wedgess.mihole.ui.logs.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.MiHoleTheme
import eu.wedgess.mihole.utils.extensions.Border
import eu.wedgess.mihole.utils.extensions.border

@Composable
fun LogsListStickyHeader(listSize: Int, onFilterButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .height(MiHoleTheme.dimens.size.logsStickyHeaderHeight)
            .border(
                bottom = Border(
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MiHoleTheme.dimens.padding.itemContentLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Results: $listSize", color = MaterialTheme.colorScheme.onSurface)

            IconButton(onClick = { onFilterButtonClicked() }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Preview
@Composable
private fun LogsListStickyHeader() {
    MiHoleTheme {
        LogsListStickyHeader(listSize = 100, onFilterButtonClicked = {})
    }
}