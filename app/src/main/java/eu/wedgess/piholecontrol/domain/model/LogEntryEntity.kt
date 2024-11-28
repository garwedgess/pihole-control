package eu.wedgess.piholecontrol.domain.model

data class LogEntryEntity(
    val timestamp: Long,
    val time: String,
    val queryType: String,
    val requestedDomain: String,
    val client: String,
    val answerType: LogAnswerTypeEntity,
    val responseTime: Int
)