package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toFilterRuleEntity
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import kotlinx.coroutines.withContext

class FilterRulesRepositoryImpl(
    private val filterRulesApiService: FilterRulesApiService,
    private val dispatcherProvider: DispatcherProvider
) : FilterRulesRepository {

    override suspend fun fetchFilterRules(
        activeConnection: ConnectionEntity,
        domainType: FilterRuleTypeEntity
    ): Result<List<FilterRuleEntity>> = withContext(dispatcherProvider.io) {
        filterRulesApiService.fetchFilterRules(
            connection = activeConnection,
            ruleType = domainType.toPiHoleFilterRuleTypeV6()
        )
            .mapCatching { response ->
                response.domains
                    .sortedBy { it.dateAdded }
                    .map { it.toFilterRuleEntity() }
            }
    }

    override suspend fun addFilterRule(
        activeConnection: ConnectionEntity,
        domain: String,
        groups: List<Int>,
        comment: String?,
        domainType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        filterRulesApiService.addFilterRule(
            connection = activeConnection,
            body = PiHoleAddFilterRuleRequestData(
                domain = domain,
                comment = comment ?: "",
                groups = groups,
                enabled = true
            ),
            ruleType = domainType.toPiHoleFilterRuleTypeV6()
        ).mapCatching { it.toEntity() }
    }

    override suspend fun removeFilterRule(
        activeConnection: ConnectionEntity,
        domain: String,
        domainType: FilterRuleTypeEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        filterRulesApiService.removeFilterRule(
            connection = activeConnection,
            rule = domain,
            ruleType = domainType.toPiHoleFilterRuleTypeV6()
        ).mapCatching {
            ModifyFilterRuleResponseEntity(
                success = true,
                message = null
            )
        }
    }
}
