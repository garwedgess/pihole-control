package eu.wedgess.piholecontrol.domain.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleLog
import eu.wedgess.piholecontrol.domain.model.LogEntryEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleLog.toLogEntryEntity() = LogEntryEntity(
    queryType = this.queryType,
    client = this.client,
    requestedDomain = this.requestedDomain,
    responseTime = this.responseTime,
    answerType = this.answerType.toLogAnswerTypeEntity(),
    timestamp = this.timestamp,
    time = Instant.ofEpochSecond(this.timestamp)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_TIME)
)