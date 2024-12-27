package eu.wedgess.piholecontrol.utils.extensions

import java.text.NumberFormat
import java.util.Locale

fun Int.formatWithThousands(): String =
    NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
