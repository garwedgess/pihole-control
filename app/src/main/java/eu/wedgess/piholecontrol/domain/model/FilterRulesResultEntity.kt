package eu.wedgess.piholecontrol.domain.model

data class FilterRulesResultEntity(
    val rules: Result<List<FilterRuleEntity>>,
    val regexRules: Result<List<FilterRuleEntity>>,
)
