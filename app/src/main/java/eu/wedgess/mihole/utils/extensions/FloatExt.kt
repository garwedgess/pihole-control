package eu.wedgess.mihole.utils.extensions

import java.text.DecimalFormat

fun Float.formatPercentage(): String =
    DecimalFormat(" #,##0.00'%'").format(this)

val Float.degreeToRadian
    get() = (this * Math.PI / 180f).toFloat()

val Float.asAngle: Float
    get() = this * 360f / 100f
