package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import eu.wedgess.piholecontrol.utils.extensions.Border
import eu.wedgess.piholecontrol.utils.extensions.border

@Composable
fun LogsListStickyHeader(listSize: Int) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .height(PiHoleControlTheme.dimens.size.logsStickyHeaderHeight)
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
                .padding(horizontal = PiHoleControlTheme.dimens.padding.itemContentLarge),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.log_results_title, listSize),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@ThemePreview
@Composable
private fun LogsListStickyHeaderPreview() {
    PiHoleControlTheme {
        LogsListStickyHeader(listSize = 100)
    }
}
