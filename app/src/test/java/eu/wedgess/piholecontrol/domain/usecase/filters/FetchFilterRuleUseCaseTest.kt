package eu.wedgess.piholecontrol.domain.usecase.filters

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.domain.usecases.filters.FetchFilterRuleUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchFilterRuleUseCaseTest {

    @MockK(relaxed = true)
    private lateinit var filterRulesRepository: FilterRulesRepository
    private lateinit var target: FetchFilterRuleUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchFilterRuleUseCase(filterRulesRepository)
    }

    @Test
    fun `invoke - returns success when repository returns filter rules`() = runTest {
        val connection = ConnectionEntity.default
        val ruleType = FilterRuleTypeEntity.ALLOW
        val filterRules = listOf<FilterRuleEntity>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )

        coEvery {
            filterRulesRepository.fetchFilterRules(connection, ruleType)
        } returns Result.success(filterRules)

        val result = target(connection, ruleType)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(filterRules)
        coVerify { filterRulesRepository.fetchFilterRules(connection, ruleType) }
    }

    @Test
    fun `invoke - returns failure when repository fetch fails`() = runTest {
        val connection = ConnectionEntity.default
        val ruleType = FilterRuleTypeEntity.ALLOW
        val exception = Exception("Failed to fetch filter rules")

        coEvery {
            filterRulesRepository.fetchFilterRules(connection, ruleType)
        } returns Result.failure(exception)

        val result = target(connection, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { filterRulesRepository.fetchFilterRules(connection, ruleType) }
    }
}
