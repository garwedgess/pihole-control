package eu.wedgess.piholecontrol.presentation.logs.extensions

import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo

fun PiHoleLogsEntity.toInfo() = LogEntryInfo(
    timestamp = this.timestamp,
    client = this.client,
    domain = this.domain,
    id = this.id,
    queryType = this.queryType,
    status = this.status,
    dnssec = this.dnssec,
    replyType = this.replyType,
    replyTime = this.replyTime,
    listId = this.listId,
    edeCode = this.edeCode,
    edeText = this.edeText,
    cname = this.cname,
    time = time
)
