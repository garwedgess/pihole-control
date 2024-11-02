package eu.wedgess.piholecontrol.ui.settings.view

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.FormatColorFill
import androidx.compose.material.icons.outlined.Lan
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.ui.settings.SettingsContract
import eu.wedgess.piholecontrol.ui.settings.model.AppTheme
import eu.wedgess.piholecontrol.ui.settings.model.mapToAppTheme
import eu.wedgess.piholecontrol.ui.settings.view.components.DropDownPreference
import eu.wedgess.piholecontrol.ui.settings.view.components.PreferenceCategory
import eu.wedgess.piholecontrol.ui.settings.view.components.RegularPreference
import eu.wedgess.piholecontrol.ui.settings.view.components.SwitchPreference
import eu.wedgess.piholecontrol.ui.settings.view.components.dialogs.SettingsDialog
import eu.wedgess.piholecontrol.ui.theme.PiHoleControlTheme
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen(
    uiState: SettingsContract.UiState,
    onEvent: (SettingsContract.Event) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PreferenceCategory(title = "User Interface")
        DropDownPreference(
            title = "Theme",
            icon = Icons.Outlined.Palette,
            items = AppTheme.entries.map { Pair(it, it.label.asString()) },
            selectedItem = uiState.currentTheme.mapToAppTheme(),
            onItemSelected = {
                onEvent(SettingsContract.Event.OnThemeChanged(it.theme))
            }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Timber.d("Dynamic Colors: ${uiState.useDynamicThemeColors}")
            SwitchPreference(
                title = "Dynamic Colors",
                icon = Icons.Outlined.FormatColorFill,
                checked = uiState.useDynamicThemeColors,
                onCheckedChange = {
                    onEvent(SettingsContract.Event.OnDynamicThemeColorsChanged(it))
                }
            )
        }

        PreferenceCategory(title = "App Settings")
        RegularPreference(
            title = "Connections",
            icon = Icons.Outlined.Lan,
            subtitle = uiState.currentConnection.name,
            onClick = {
                onEvent(SettingsContract.Event.OnServerClicked)
            })
        RegularPreference(
            title = "Data Refresh Interval",
            icon = Icons.Outlined.Update,
            subtitle = "${
                TimeUnit.SECONDS.convert(
                    uiState.refreshInterval,
                    TimeUnit.MILLISECONDS
                )
            } seconds",
            onClick = { onEvent(SettingsContract.Event.OnRefreshIntervalClicked) }
        )

        SwitchPreference(
            title = "Multi-status Change",
            subtitle = "Apply status changes to all connections",
            icon = Icons.Outlined.Devices,
            checked = uiState.changeStatusOnAllConnections,
            onCheckedChange = {
                Timber.d("Dynamic Colors firing checked changes")
                onEvent(SettingsContract.Event.OnChangeStatusOnAllConnectionsChanged(it))
            }
        )

        SettingsDialog(uiState.dialogType, onEvent)
    }
}


@Preview
@Composable
private fun SettingsScreenPreview() {
    PiHoleControlTheme {
        Surface() {
            SettingsScreen(uiState = SettingsContract.UiState.initial(), onEvent = {})
        }
    }
}