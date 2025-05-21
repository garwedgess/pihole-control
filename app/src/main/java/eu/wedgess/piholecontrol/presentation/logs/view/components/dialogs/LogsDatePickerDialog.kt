package eu.wedgess.piholecontrol.presentation.logs.view.components.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.presentation.compose.PastOrPresentSelectableDates
import eu.wedgess.piholecontrol.presentation.compose.ThemePreview
import eu.wedgess.piholecontrol.presentation.theme.PiHoleControlTheme
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (date: LocalDate) -> Unit,
    selectedDate: LocalDate? = null
) {
    val state = rememberDatePickerState(
        selectableDates = PastOrPresentSelectableDates,
        initialSelectedDateMillis = selectedDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)
            ?.toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val date = state.selectedDateMillis?.run {
                        Instant
                            .ofEpochMilli(this@run)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: LocalDate.now()
                    onConfirm(date)
                }
            ) {
                Text(text = stringResource(R.string.all_btn_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.all_btn_cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@ThemePreview
@Composable
private fun LogsDatePickerDialogPreview() {
    PiHoleControlTheme {
        LogsDatePickerDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
