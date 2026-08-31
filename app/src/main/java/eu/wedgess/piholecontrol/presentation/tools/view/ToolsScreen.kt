package eu.wedgess.piholecontrol.presentation.tools.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.settings.view.components.PreferenceCategory
import eu.wedgess.piholecontrol.presentation.settings.view.components.RegularPreference

@Composable
fun ToolsScreen(
    onLocalDnsClick: () -> Unit,
    onDiagnosisClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PreferenceCategory(title = stringResource(R.string.tools_category_pihole))
        RegularPreference(
            title = stringResource(R.string.tools_local_dns_title),
            icon = Icons.Outlined.Dns,
            subtitle = stringResource(R.string.tools_local_dns_subtitle),
            onClick = onLocalDnsClick
        )
        RegularPreference(
            title = stringResource(R.string.tools_diagnosis_title),
            icon = Icons.Outlined.ReportProblem,
            subtitle = stringResource(R.string.tools_diagnosis_subtitle),
            onClick = onDiagnosisClick
        )
    }
}
