package eu.wedgess.piholecontrol.presentation.localdns.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import eu.wedgess.piholecontrol.presentation.localdns.model.LocalDnsRecordInfo
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun LocalDnsRecordItem(
    record: LocalDnsRecordInfo,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onItemClick() }
            .padding(
                horizontal = PiHoleControlTheme.dimens.padding.screenContent,
                vertical = PiHoleControlTheme.dimens.padding.itemContent
            ),
        verticalArrangement = Arrangement.spacedBy(
            PiHoleControlTheme.dimens.padding.itemContentXSmall
        )
    ) {
        Text(
            text = record.domain,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = record.ipAddress,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Normal,
                color = LocalContentColor.current.copy(
                    alpha = PiHoleControlTheme.dimens.weight.secondaryTextAlpha
                )
            )
        )
    }
}
