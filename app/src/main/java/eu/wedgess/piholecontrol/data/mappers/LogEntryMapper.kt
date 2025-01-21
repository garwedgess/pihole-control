package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun PiHoleLogsResponseDataV5.PiHoleLogEntryData.toLogEntryEntity() = PiHoleLogsEntity.Version5(
    queryType = this.queryType,
    client = this.client,
    domain = this.requestedDomain,
    replyTime = this.responseTime.toDouble(),
    answerType = PiHoleLogsEntity.LogsAnswerTypeEntity[this.answerType.key],
    timestamp = this.timestamp,
    time = Instant.ofEpochSecond(this.timestamp)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_LOCAL_TIME)
)

fun PiHoleLogsResponseDataV6.PiHoleLogEntryData.toLogEntryEntity() = PiHoleLogsEntity.Version6(
    timestamp = this.time.toLong(),
    client = this.client.combinedName,
    domain = this.domain,
    id = this.id,
    type = PiHoleLogsEntity.LogEntryTypeEntity[this.type.key],
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
