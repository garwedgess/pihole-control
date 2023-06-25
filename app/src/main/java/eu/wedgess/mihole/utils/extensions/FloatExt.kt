package eu.wedgess.mihole.utils.extensions

import java.text.DecimalFormat

fun Float.formatPercentage(): String =
    DecimalFormat(" #,##0.00'%'").format(this)