package eu.wedgess.piholecontrol.presentation.settings.view

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.FormatColorFill
import androidx.compose.material.icons.outlined.Lan
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.settings.SettingsContract
import eu.wedgess.piholecontrol.presentation.settings.model.AppThemePres
import eu.wedgess.piholecontrol.presentation.settings.view.components.DropDownPreference
import eu.wedgess.piholecontrol.presentation.settings.view.components.PreferenceCategory
import eu.wedgess.piholecontrol.presentation.settings.view.components.RegularPreference
import eu.wedgess.piholecontrol.presentation.settings.view.components.SwitchPreference
import eu.wedgess.piholecontrol.presentation.settings.view.components.dialogs.SettingsDialog
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen(
    uiState: SettingsContract.UiState,
    onEvent: (SettingsContract.Event) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PreferenceCategory(title = stringResource(R.string.settings_category_user_interface))
        DropDownPreference(
            title = stringResource(R.string.settings_theme_title),
            icon = Icons.Outlined.Palette,
            items = AppThemePres.entries.map { Pair(it, it.label.asString()) },
            selectedItem = uiState.currentTheme,
            onItemClick = {
                onEvent(SettingsContract.Event.OnThemeChanged(it))
            }
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Timber.d("Dynamic Colors: ${uiState.useDynamicThemeColors}")
            SwitchPreference(
                title = stringResource(R.string.settings_dynamic_colors_title),
                icon = Icons.Outlined.FormatColorFill,
                checked = uiState.useDynamicThemeColors,
                onCheckedChange = {
                    onEvent(SettingsContract.Event.OnDynamicThemeColorsChanged(it))
                }
            )
        }

        PreferenceCategory(title = stringResource(R.string.settings_category_app_settings))
        RegularPreference(
            title = stringResource(R.string.settings_connections_title),
            icon = Icons.Outlined.Lan,
            subtitle = uiState.currentConnection.name,
            onClick = {
                onEvent(SettingsContract.Event.OnServerClicked)
            }
        )
        RegularPreference(
            title = stringResource(R.string.local_dns_settings_title),
            icon = Icons.Outlined.Lan,
            subtitle = stringResource(R.string.local_dns_settings_subtitle),
            onClick = {
                onEvent(SettingsContract.Event.OnLocalDnsClicked)
            }
        )
        RegularPreference(
            title = stringResource(R.string.diagnosis_settings_title),
            icon = Icons.Outlined.ReportProblem,
            subtitle = stringResource(R.string.diagnosis_settings_subtitle),
            onClick = {
                onEvent(SettingsContract.Event.OnDiagnosisClicked)
            }
        )
        RegularPreference(
            title = stringResource(R.string.settings_data_refresh_interval_title),
            icon = Icons.Outlined.Update,
            subtitle = stringResource(
                R.string.settings_refresh_interval_seconds,
                TimeUnit.SECONDS.convert(
                    uiState.refreshInterval,
                    TimeUnit.MILLISECONDS
                )
            ),
            onClick = {
                onEvent(SettingsContract.Event.OnRefreshIntervalClicked(uiState.refreshInterval))
            }
        )

        SwitchPreference(
            title = stringResource(R.string.settings_multi_status_change_title),
            subtitle = stringResource(R.string.settings_multi_status_change_subtitle),
            icon = Icons.Outlined.Devices,
            checked = uiState.changeStatusOnAllConnections,
            onCheckedChange = {
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
        Surface {
            SettingsScreen(uiState = SettingsContract.UiState.initial(), onEvent = {})
        }
    }
}
