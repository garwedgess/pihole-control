package eu.wedgess.piholecontrol.domain.model


enum class LogAnswerTypeEntity(val category: LogAnswerCategoryEntity) {
    GRAVITY_BLOCK(LogAnswerCategoryEntity.BLOCK),
    UPSTREAM(LogAnswerCategoryEntity.ALLOW),
    LOCAL_CACHE(LogAnswerCategoryEntity.CACHE),
    REGEX_BLOCK(LogAnswerCategoryEntity.BLOCK),
    EXACT_BLOCK(LogAnswerCategoryEntity.BLOCK),
    EXTERNAL_IP_BLOCK(LogAnswerCategoryEntity.BLOCK),
    EXTERNAL_NULL_BLOCK(LogAnswerCategoryEntity.BLOCK),
    EXTERNAL_NXRA_BLOCK(LogAnswerCategoryEntity.BLOCK),
    CNAME_GRAVITY_BLOCK(LogAnswerCategoryEntity.BLOCK),
    CNAME_REGEX_BLOCK(LogAnswerCategoryEntity.BLOCK),
    CNAME_EXACT_BLOCK(LogAnswerCategoryEntity.BLOCK),
    RETRIED(LogAnswerCategoryEntity.ALLOW),
    RETRIED_IGNORED(LogAnswerCategoryEntity.ALLOW),
    ALREADY_FORWARDED(LogAnswerCategoryEntity.ALLOW),
    UNKNOWN(LogAnswerCategoryEntity.UNKNOWN)
}