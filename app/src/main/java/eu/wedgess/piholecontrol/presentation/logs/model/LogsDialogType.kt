package eu.wedgess.piholecontrol.presentation.logs.model

import org.threeten.bp.LocalDate

sealed interface LogsDialogType {
    data object None : LogsDialogType
    data class ShowDetailsDialog(val details: LogEntryInfo) : LogsDialogType
    data class ShowDatePickerDialog(val pickerType: PickerType) : LogsDialogType
    data class ShowTimePickerDialog(val pickerType: PickerType, val currentDate: LocalDate) :
        LogsDialogType
}
