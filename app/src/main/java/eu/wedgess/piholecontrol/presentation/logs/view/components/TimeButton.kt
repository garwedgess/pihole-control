package eu.wedgess.piholecontrol.presentation.logs.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme

@Composable
fun TimeButton(
    title: String,
    subTitle: String?,
    onClick: () -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadius))
            .border(
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(PiHoleControlTheme.dimens.size.cornerRadius)
            )
            .width(PiHoleControlTheme.dimens.size.timeButtonWidth)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PiHoleControlTheme.dimens.padding.itemContent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(PiHoleControlTheme.dimens.weight.full),
                text = title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleSmall
            )
            AnimatedVisibility(visible = subTitle.isNullOrBlank().not()) {
                Surface(
                    modifier = Modifier,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    IconButton(
                        onClick = {
                            onClearClick()
                        },
                        modifier = Modifier
                            .size(PiHoleControlTheme.dimens.padding.itemContentLarge)
                            .padding(DividerDefaults.Thickness)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Text(
            modifier = Modifier.padding(
                bottom = PiHoleControlTheme.dimens.padding.itemContent,
                start = PiHoleControlTheme.dimens.padding.itemContent,
                end = PiHoleControlTheme.dimens.padding.itemContent
            ),
            text = subTitle ?: stringResource(R.string.logs_filter_sheet_time_button_value_not_set),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun TimeButtonPreview() {
    PiHoleControlTheme {
        TimeButton(
            title = "From Time",
            subTitle = "12-12-2023 12:45",
            onClick = {},
            onClearClick = {}
        )
    }
}
