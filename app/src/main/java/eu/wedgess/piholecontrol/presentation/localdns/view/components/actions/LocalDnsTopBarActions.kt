package eu.wedgess.piholecontrol.presentation.localdns.view.components.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun LocalDnsTopBarActions(
    onSearchClick: () -> Unit
) {
    IconButton(onClick = onSearchClick) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}
