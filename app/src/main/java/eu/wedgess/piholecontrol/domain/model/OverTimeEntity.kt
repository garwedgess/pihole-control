package eu.wedgess.piholecontrol.domain.model

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


data class OverTimeEntity(val timestamp: Long, val hits: Long) {
    val time: String
        get() = Instant.ofEpochSecond(timestamp)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_TIME)
}