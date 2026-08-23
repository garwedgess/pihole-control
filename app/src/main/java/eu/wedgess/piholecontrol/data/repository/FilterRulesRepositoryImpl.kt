package eu.wedgess.piholecontrol.data.repository

import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toFilterRuleEntity
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleTypeV6
import eu.wedgess.piholecontrol.data.model.requests.PiHoleAddFilterRuleRequestData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleUpdateEntity
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
        domainType: FilterRuleTypeEntity,
        enabled: Boolean
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        filterRulesApiService.addFilterRule(
            connection = activeConnection,
            body = createFilterRuleRequest(domain, groups, comment, enabled),
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

    override suspend fun updateFilterRule(
        activeConnection: ConnectionEntity,
        update: FilterRuleUpdateEntity
    ): Result<ModifyFilterRuleResponseEntity> = withContext(dispatcherProvider.io) {
        if (update.originalType == update.type) {
            updateExistingFilterRule(
                activeConnection = activeConnection,
                update = update
            )
        } else {
            replaceFilterRule(
                activeConnection = activeConnection,
                update = update
            )
        }
    }

    private suspend fun updateExistingFilterRule(
        activeConnection: ConnectionEntity,
        update: FilterRuleUpdateEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return filterRulesApiService.updateFilterRule(
            connection = activeConnection,
            rule = update.originalDomain,
            body = createFilterRuleRequest(update),
            ruleType = update.type.toPiHoleFilterRuleTypeV6()
        ).mapCatching { successModifyFilterRuleResponse() }
    }

    private suspend fun replaceFilterRule(
        activeConnection: ConnectionEntity,
        update: FilterRuleUpdateEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return filterRulesApiService.addFilterRule(
            connection = activeConnection,
            body = createFilterRuleRequest(update),
            ruleType = update.type.toPiHoleFilterRuleTypeV6()
        ).fold(
            onSuccess = {
                removeOriginalFilterRule(
                    activeConnection = activeConnection,
                    update = update
                )
            },
            onFailure = { error -> Result.failure(error) }
        )
    }

    private suspend fun removeOriginalFilterRule(
        activeConnection: ConnectionEntity,
        update: FilterRuleUpdateEntity
    ): Result<ModifyFilterRuleResponseEntity> {
        return filterRulesApiService.removeFilterRule(
            connection = activeConnection,
            rule = update.originalDomain,
            ruleType = update.originalType.toPiHoleFilterRuleTypeV6()
        ).mapCatching { successModifyFilterRuleResponse() }
    }

    private fun createFilterRuleRequest(update: FilterRuleUpdateEntity) = createFilterRuleRequest(
        domain = update.domain,
        groups = update.groups,
        comment = update.comment,
        enabled = update.enabled
    )

    private fun createFilterRuleRequest(
        domain: String,
        groups: List<Int>,
        comment: String?,
        enabled: Boolean
    ) = PiHoleAddFilterRuleRequestData(
        domain = domain,
        comment = comment ?: "",
        groups = groups,
        enabled = enabled
    )

    private fun successModifyFilterRuleResponse() = ModifyFilterRuleResponseEntity(
        success = true,
        message = null
    )
}
