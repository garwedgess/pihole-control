package eu.wedgess.mihole.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.ui.base.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(appBarState: AppBarState, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = appBarState.title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            AnimatedVisibility(visible = appBarState.showSearchView) {
                appBarState.searchContent?.invoke()
            }
            AnimatedVisibility(visible = !appBarState.showSearchView) {
                appBarState.actions?.invoke(this@TopAppBar)
            }
        },
        navigationIcon = {
            if (appBarState.showNavigateBackIcon) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    )
}