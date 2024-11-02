package eu.wedgess.mihole.ui.logs.model

import eu.wedgess.mihole.data.model.responses.PiHoleLog
import org.threeten.bp.LocalDate

sealed interface LogsDialogType {
    data object None : LogsDialogType
    data class ShowDetailsDialog(val details: PiHoleLog) : LogsDialogType
    data class ShowDatePickerDialog(val pickerType: PickerType) : LogsDialogType
    data class ShowTimePickerDialog(val pickerType: PickerType, val currentDate: LocalDate) :
        LogsDialogType
}