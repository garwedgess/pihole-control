package eu.wedgess.mihole.ui.logs.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.ui.graphics.vector.ImageVector
import eu.wedgess.mihole.R
import eu.wedgess.mihole.utils.UiText

enum class LogSorting(val uiText: UiText, val icon: ImageVector) {
    DATE_DESC(UiText.StringResource(R.string.log_label_date_sort_desc), Icons.Default.ArrowUpward),
    DATE_ASC(UiText.StringResource(R.string.log_label_date_sort_asc), Icons.Default.ArrowDownward),
    RESPONSE_TIME_ASC(UiText.StringResource(R.string.log_label_response_time_sort_asc), Icons.Default.HourglassBottom),
    RESPONSE_TIME_DESC(UiText.StringResource(R.string.log_label_response_time_sort_desc), Icons.Default.HourglassTop)
}