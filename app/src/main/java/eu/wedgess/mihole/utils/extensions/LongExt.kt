package eu.wedgess.mihole.utils.extensions

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


fun Long.toDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
    val instant = Instant.ofEpochSecond(this)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return formatter.format(date)
}

fun Long.epochMillisToCurrentTimezoneEpochSeconds(): Long = Instant.ofEpochMilli(this)
    .atOffset(OffsetDateTime.now().offset).toEpochSecond()