package eu.wedgess.mihole.ui.filters.view

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import eu.wedgess.mihole.ui.filters.FiltersContract
import eu.wedgess.mihole.ui.filters.view.components.FilterScreenContent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FiltersScreen(
    uiState: FiltersContract.UiState,
    onEvent: (FiltersContract.Event) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(FiltersContract.Event.OnAddRuleClick) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        content = {
            FilterScreenContent(uiState, onEvent)
        }
    )
}