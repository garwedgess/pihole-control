package eu.wedgess.piholecontrol.domain.usecases.filters

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.model.ModifyFilterRuleResponseEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.FilterRulesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoveFilterRuleUseCaseTest {

    @MockK
    private lateinit var filterRuleRepository: FilterRulesRepository

    @MockK
    private lateinit var connectionRepository: ConnectionRepository
    private lateinit var target: RemoveFilterRuleUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = RemoveFilterRuleUseCase(filterRuleRepository, connectionRepository)
    }

    @Test
    fun `invoke - returns success when rule is removed successfully`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val connection = ConnectionEntity.Version5.default
        val expectedResponse = ModifyFilterRuleResponseEntity(success = true, message = null)
        coEvery { connectionRepository.fetchActive() } returns Result.success(connection)
        coEvery {
            filterRuleRepository.removeFilterRule(connection, rule, ruleType)
        } returns Result.success(expectedResponse)

        val result = target(rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedResponse)
        coVerify { connectionRepository.fetchActive() }
        coVerify { filterRuleRepository.removeFilterRule(connection, rule, ruleType) }
    }

    @Test
    fun `invoke - returns failure when fetchActive fails`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val exception = Exception("No active connection found")
        coEvery { connectionRepository.fetchActive() } returns Result.failure(exception)

        val result = target(rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { connectionRepository.fetchActive() }
        coVerify(exactly = 0) { filterRuleRepository.removeFilterRule(any(), any(), any()) }
    }

    @Test
    fun `invoke - returns failure when removeFilterRule fails`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val connection = ConnectionEntity.Version5.default
        val exception = Exception("Failed to remove filter rule")
        coEvery { connectionRepository.fetchActive() } returns Result.success(connection)
        coEvery {
            filterRuleRepository.removeFilterRule(connection, rule, ruleType)
        } returns Result.failure(exception)

        val result = target(rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { connectionRepository.fetchActive() }
        coVerify { filterRuleRepository.removeFilterRule(connection, rule, ruleType) }
    }
}
