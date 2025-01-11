package eu.wedgess.piholecontrol.presentation.logs.extensions

import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo

fun PiHoleLogsEntity.toInfo() = when (this) {
    is PiHoleLogsEntity.Version5 -> {
        LogEntryInfo.Version5(
            queryType = this.queryType,
            client = this.client,
            domain = this.domain,
            replyTime = this.replyTime,
            answerType = this.answerType,
            timestamp = this.timestamp,
            time = this.time
        )
    }

    is PiHoleLogsEntity.Version6 -> {
        LogEntryInfo.Version6(
            timestamp = this.timestamp,
            client = this.client,
            domain = this.domain,
            id = this.id,
            type = this.type,
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
    }
}
