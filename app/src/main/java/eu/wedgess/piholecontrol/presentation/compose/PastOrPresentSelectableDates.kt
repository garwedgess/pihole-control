package eu.wedgess.piholecontrol.presentation.compose;

import androidx.compose.material3.ExperimentalMaterial3Api;
import androidx.compose.material3.SelectableDates
import org.threeten.bp.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis <= System.currentTimeMillis()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year <= LocalDate.now().year
    }
}