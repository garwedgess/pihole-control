package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.FilterRulesApiService
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FilterRulesRepositoryImplTest {

    @MockK
    private lateinit var apiService: FilterRulesApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: FilterRulesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = FilterRulesRepositoryImpl(apiService, dispatcherProvider)
    }

    @Test
    fun `fetchFilterRules - api fetchFilterRules is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        coEvery {
            apiService.fetchFilterRules(
                activeConnection,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.success(
            mockk(relaxed = true)
        )

        val result = target.fetchFilterRules(activeConnection, ruleType)

        assertThat(result.isSuccess).isTrue()
        val rules = result.getOrNull()
        assertThat(rules).isNotNull()
        coVerify {
            apiService.fetchFilterRules(
                activeConnection,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `fetchFilterRules - api fetchFilterRules is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery {
            apiService.fetchFilterRules(
                activeConnection,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.failure(
            exception
        )

        val result = target.fetchFilterRules(activeConnection, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify {
            apiService.fetchFilterRules(
                activeConnection,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `addFilterRule - api addFilterRule is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        coEvery {
            apiService.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.success(
            mockk(relaxed = true)
        )

        val result = target.addFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        val response = result.getOrNull()
        assertThat(response).isNotNull()
        coVerify {
            apiService.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `addFilterRule - api addFilterRule is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery {
            apiService.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.failure(
            exception
        )

        val result = target.addFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify {
            apiService.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `removeFilterRule - api removeFilterRule is invoked AND result is success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        coEvery {
            apiService.removeFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.success(
            mockk(relaxed = true)
        )

        val result = target.removeFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        val response = result.getOrNull()
        assertThat(response).isNotNull()
        coVerify {
            apiService.removeFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `removeFilterRule - api removeFilterRule is invoked AND result is failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery {
            apiService.removeFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.failure(
            exception
        )

        val result = target.removeFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify {
            apiService.removeFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }
}
