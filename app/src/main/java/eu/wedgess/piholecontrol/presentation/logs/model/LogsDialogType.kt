package eu.wedgess.piholecontrol.presentation.logs.model

import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import org.threeten.bp.LocalDate

sealed interface LogsDialogType {
    data object None : LogsDialogType
    data class ShowDetailsDialog(val details: LogEntryEntity) : LogsDialogType
    data class ShowDatePickerDialog(val pickerType: PickerType) : LogsDialogType
    data class ShowTimePickerDialog(val pickerType: PickerType, val currentDate: LocalDate) :
        LogsDialogType
}