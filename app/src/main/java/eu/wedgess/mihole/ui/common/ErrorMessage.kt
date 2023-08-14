package eu.wedgess.mihole.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.mihole.R
import eu.wedgess.mihole.ui.theme.MiHoleTheme

@Composable
fun ErrorMessage(errorMessage: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(MiHoleTheme.dimens.padding.screenContent),
        verticalArrangement = Arrangement.spacedBy(
            MiHoleTheme.dimens.padding.itemContent,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { onRetry.invoke() }
        ) {
            Text(stringResource(R.string.all_btn_retry))
        }
    }
}