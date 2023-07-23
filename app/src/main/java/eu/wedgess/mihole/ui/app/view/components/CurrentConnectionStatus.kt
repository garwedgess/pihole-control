package eu.wedgess.mihole.ui.app.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.wedgess.mihole.data.model.MiHolesInfo
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun CurrentConnectionStatus(
    modifier: Modifier,
    currentConnection: MiHolesInfo
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(onClick = { }) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.GppGood,
                contentDescription = ""
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = currentConnection.host,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
            Text(
                text = currentConnection.name,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = MiHoleTheme.dimens.weight.secondaryTextAlpha),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}


@Preview
@Composable
private fun CurrentConnectionStatusPreview() {
    MiHoleTheme {
        CurrentConnectionStatus(modifier = Modifier, currentConnection = MiHolesInfo.default)
    }
}