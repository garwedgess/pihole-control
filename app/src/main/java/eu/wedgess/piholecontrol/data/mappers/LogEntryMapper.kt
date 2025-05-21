package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogsResponseData
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleLogsResponseData.PiHoleLogEntryData.toLogEntryEntity() = PiHoleLogsEntity(
    timestamp = this.time.toLong(),
    client = this.client.combinedName,
    domain = this.domain,
    id = this.id,
    queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity[this.type.key],
    status = PiHoleLogsEntity.LogEntryStatusEntity[this.status.key],
    dnssec = PiHoleLogsEntity.LogEntryDnssecEntity[this.dnssec.key],
    replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity[this.reply.type.key],
    replyTime = this.reply.time,
    listId = this.listId,
    edeCode = this.ede.code,
    edeText = this.ede.text,
    cname = this.cname,
    time = Instant.ofEpochSecond(this.time.toLong())
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_TIME)
)
