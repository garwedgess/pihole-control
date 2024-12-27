package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity

fun LogsAnswerType.toLogAnswerTypeEntity() = when (this) {
    LogsAnswerType.GRAVITY_BLOCK -> LogAnswerTypeEntity.GRAVITY_BLOCK
    LogsAnswerType.UPSTREAM -> LogAnswerTypeEntity.UPSTREAM
    LogsAnswerType.LOCAL_CACHE -> LogAnswerTypeEntity.LOCAL_CACHE
    LogsAnswerType.REGEX_BLOCK -> LogAnswerTypeEntity.REGEX_BLOCK
    LogsAnswerType.EXACT_BLOCK -> LogAnswerTypeEntity.EXACT_BLOCK
    LogsAnswerType.EXTERNAL_IP_BLOCK -> LogAnswerTypeEntity.EXTERNAL_IP_BLOCK
    LogsAnswerType.EXTERNAL_NULL_BLOCK -> LogAnswerTypeEntity.EXTERNAL_NULL_BLOCK
    LogsAnswerType.EXTERNAL_NXRA_BLOCK -> LogAnswerTypeEntity.EXTERNAL_NXRA_BLOCK
    LogsAnswerType.CNAME_GRAVITY_BLOCK -> LogAnswerTypeEntity.CNAME_GRAVITY_BLOCK
    LogsAnswerType.CNAME_REGEX_BLOCK -> LogAnswerTypeEntity.CNAME_REGEX_BLOCK
    LogsAnswerType.CNAME_EXACT_BLOCK -> LogAnswerTypeEntity.CNAME_EXACT_BLOCK
    LogsAnswerType.RETRIED -> LogAnswerTypeEntity.RETRIED
    LogsAnswerType.RETRIED_IGNORED -> LogAnswerTypeEntity.RETRIED_IGNORED
    LogsAnswerType.ALREADY_FORWARDED -> LogAnswerTypeEntity.ALREADY_FORWARDED
    LogsAnswerType.UNKNOWN -> LogAnswerTypeEntity.UNKNOWN
}
