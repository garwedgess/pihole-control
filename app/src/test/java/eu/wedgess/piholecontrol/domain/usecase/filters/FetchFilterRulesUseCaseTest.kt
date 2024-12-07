package eu.wedgess.piholecontrol.domain.usecase.filters

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRuleUseCase
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRulesUseCase
import eu.wedgess.piholecontrol.presentation.filters.tab.model.FilterRulesResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchFilterRulesUseCaseTest {

    private lateinit var fetchFilterRuleUseCase: FetchFilterRuleUseCase
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase
    private lateinit var target: FetchFilterRulesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fetchFilterRuleUseCase = mockk()
        periodicRefreshUseCase = mockk()
        target = FetchFilterRulesUseCase(fetchFilterRuleUseCase, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - returns combined filter and regex filter rules`() = runTest {
        val connection = ConnectionEntity.default
        val ruleType = FilterRuleTypeEntity.ALLOW
        val rules = Result.success(listOf<FilterRuleEntity>(mockk(relaxed = true), mockk(relaxed = true)))
        val regexRules = Result.success(listOf<FilterRuleEntity>(mockk(relaxed = true), mockk(relaxed = true)))

        coEvery { fetchFilterRuleUseCase(connection, FilterRuleTypeEntity.ALLOW) } returns rules
        coEvery { fetchFilterRuleUseCase(connection, FilterRuleTypeEntity.REGEX_ALLOW) } returns regexRules
        coEvery { periodicRefreshUseCase<Result<FilterRulesResult>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<FilterRulesResult>>(0)
                emit(fetchBlock(connection))
            }
        }

        val result = target(ruleType).first()

        assertThat(result.isSuccess).isTrue()
        val data = result.getOrNull()!!
        assertThat(data.rules).isEqualTo(rules)
        assertThat(data.regexRules).isEqualTo(regexRules)
    }

    @Test
    fun `invoke - handles error in one of the rule types`() = runTest {
        val connection = ConnectionEntity.default
        val ruleType = FilterRuleTypeEntity.BLOCK
        val rules = Result.success(listOf<FilterRuleEntity>(mockk(relaxed = true), mockk(relaxed = true)))
        val regexRulesException = Exception("Failed to fetch regex rules")

        coEvery { fetchFilterRuleUseCase(connection, FilterRuleTypeEntity.BLOCK) } returns rules
        coEvery { fetchFilterRuleUseCase(connection, FilterRuleTypeEntity.REGEX_BLOCK) } returns Result.failure(regexRulesException)
        coEvery { periodicRefreshUseCase<Result<FilterRulesResult>>(any()) } answers {
            flow {
                val fetchBlock = arg<suspend (ConnectionEntity) -> Result<FilterRulesResult>>(0)
                emit(fetchBlock(connection))
            }
        }

        val result = target(ruleType).first()

        assertThat(result.isSuccess).isTrue()
        val data = result.getOrNull()
        assertThat(data?.rules).isEqualTo(rules)
        assertThat(data?.regexRules?.isFailure).isTrue()
        assertThat(data?.regexRules?.exceptionOrNull()).isEqualTo(regexRulesException)
    }

    @Test
    fun `refreshRules - triggers refresh on periodicRefreshUseCase`() {
        every { periodicRefreshUseCase.triggerRefresh() } returns true

        target.refreshRules()

        verify { periodicRefreshUseCase.triggerRefresh() }
    }
}
