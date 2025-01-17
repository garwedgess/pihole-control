package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.FilterRulesApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toFilterRuleEntity
import eu.wedgess.piholecontrol.data.mappers.toModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestDataV6
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleCombinedFilterRulesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAddFilterRuleResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleFilterRulesResponseDataV6
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class FilterRulesRepositoryImpl(
    private val apiServiceV5: FilterRulesApiServiceV5,
    private val apiServiceV6: FilterRulesApiServiceV6,
    private val dispatcherProvider: DispatcherProvider
) : FilterRulesRepository {

    override suspend fun fetchFilterRules(
        activeConnection: ConnectionEntity,
        ruleType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeConnection,
            v5Call = {
                apiServiceV5.fetchCombinedFilterRules(
                    connection = it,
                    ruleType = ruleType.toPiHoleFilterRuleType()
                )
            },
            v6Call = { apiServiceV6.fetchFilterRules(it, ruleType.toPiHoleFilterRuleTypeV6()) },
            mapper = { response ->
                when (response) {
                    is PiHoleCombinedFilterRulesResponseDataV5 -> {
                        (response.rules + response.regexRules)
                            .sortedBy { it.dateAdded }
                            .map { it.toFilterRuleEntity() }
                    }

                    is PiHoleFilterRulesResponseDataV6 -> {
                        response.domains
                            .sortedBy { it.dateAdded }
                            .map { it.toFilterRuleEntity() }
                    }

                    else -> {
                        throw IllegalArgumentException("Unknown type: ${response.javaClass.name}")
                    }
                }
            }
        )
    }

    override suspend fun addFilterRule(
        activeConnection: ConnectionEntity,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeConnection,
            v5Call = {
                apiServiceV5.addFilterRule(
                    connection = it,
                    rule = rule,
                    ruleType = ruleType.toPiHoleFilterRuleType()
                )
            },
            v6Call = {
                apiServiceV6.addFilterRule(
                    connection = it,
                    body = PiHoleAddFilterRuleRequestDataV6(
                        domain = rule,
                        comment = "",
                        groups = emptyList(),
                        enabled = true
                    ),
                    ruleType = ruleType.toPiHoleFilterRuleTypeV6()
                )
            },
            mapper = { response ->
                when (response) {
                    is PiHoleModifyFilterRuleResponseDataV5 -> {
                        response.toModifyFilterRuleResponseEntity()
                    }

                    is PiHoleAddFilterRuleResponseDataV6 -> {
                        response.toModifyFilterRuleResponseEntity()
                    }

                    else -> {
                        throw IllegalArgumentException("Unknown type: ${response.javaClass.name}")
                    }
                }
            }
        )
    }

    override suspend fun removeFilterRule(
        activeConnection: ConnectionEntity,
        rule: String,
        ruleType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        callVersionedEndpoint(
            activeConnection = activeConnection,
            v5Call = {
                apiServiceV5.removeFilterRule(
                    connection = it,
                    rule = rule,
                    ruleType = ruleType.toPiHoleFilterRuleType()
                )
            },
            v6Call = {
                apiServiceV6.removeFilterRule(
                    connection = it,
                    rule = rule,
                    ruleType = ruleType.toPiHoleFilterRuleTypeV6()
                )
            },
            mapper = { response ->
                when (response) {
                    is PiHoleModifyFilterRuleResponseDataV5 -> {
                        response.toModifyFilterRuleResponseEntity()
                    }

                    is Unit -> ModifyFilterRuleResponseEntity(
                        success = true,
                        message = null
                    )

                    else -> {
                        throw IllegalArgumentException("Unknown type: ${response.javaClass.name}")
                    }
                }
            }
        )
    }
}
