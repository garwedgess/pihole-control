package eu.wedgess.mihole.ui.logs.view.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun TimeButton(
    title: String,
    subTitle: String?,
    onClick: () -> Unit,
    onClearClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .width(164.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
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
                            onClearClicked()
                        },
                        modifier = Modifier
                            .size(16.dp)
                            .padding(1.dp)
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
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            text = subTitle ?: "Not set",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun TimeButtonPreview() {
    MiHoleTheme {
        TimeButton(
            title = "From Time",
            subTitle = "12-12-2023 12:45",
            onClick = {},
            onClearClicked = {}
        )
    }
}