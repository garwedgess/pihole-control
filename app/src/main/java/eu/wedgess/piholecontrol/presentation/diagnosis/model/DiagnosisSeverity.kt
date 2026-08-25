package eu.wedgess.piholecontrol.presentation.diagnosis.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.utils.UiText

enum class DiagnosisSeverity {
    Error,
    Warning,
    Info
}

fun String.toDiagnosisSeverity(): DiagnosisSeverity {
    val normalizedType = lowercase()
    return when {
        "error" in normalizedType || "fatal" in normalizedType -> DiagnosisSeverity.Error
        "warn" in normalizedType || "dnsmasq" in normalizedType -> DiagnosisSeverity.Warning
        else -> DiagnosisSeverity.Info
    }
}

fun DiagnosisSeverity.label(): UiText = when (this) {
    DiagnosisSeverity.Error -> UiText.StringResource(R.string.diagnosis_severity_error)
    DiagnosisSeverity.Warning -> UiText.StringResource(R.string.diagnosis_severity_warning)
    DiagnosisSeverity.Info -> UiText.StringResource(R.string.diagnosis_severity_info)
}

fun DiagnosisSeverity.icon(): ImageVector = when (this) {
    DiagnosisSeverity.Error -> Icons.Outlined.ErrorOutline
    DiagnosisSeverity.Warning -> Icons.Outlined.WarningAmber
    DiagnosisSeverity.Info -> Icons.Outlined.Info
}

@Composable
fun DiagnosisSeverity.color(): Color = when (this) {
    DiagnosisSeverity.Error -> androidx.compose.material3.MaterialTheme.colorScheme.error
    DiagnosisSeverity.Warning -> androidx.compose.material3.MaterialTheme.colorScheme.tertiary
    DiagnosisSeverity.Info -> androidx.compose.material3.MaterialTheme.colorScheme.primary
}
