package eu.wedgess.piholecontrol.utils.extensions

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun Long.toDateString(zoneId: ZoneId = ZoneId.systemDefault()): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(this), zoneId)
    return dateTime.format(formatter)
}

fun LocalDateTime.toEpochSeconds(zoneId: ZoneId = ZoneId.systemDefault()) =
    this.atZone(zoneId).toInstant().epochSecond
