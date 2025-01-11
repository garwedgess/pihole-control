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

class AddFilterRuleUseCaseTest {

    @MockK
    private lateinit var filterRuleRepository: FilterRulesRepository

    @MockK
    private lateinit var connectionRepository: ConnectionRepository

    private lateinit var target: AddFilterRuleUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = AddFilterRuleUseCase(filterRuleRepository, connectionRepository)
    }

    @Test
    fun `invoke - returns success when filter rule is added successfully`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val connection = ConnectionEntity.Version5.default
        val response = ModifyFilterRuleResponseEntity(success = true, message = null)

        coEvery { connectionRepository.fetchActive() } returns Result.success(connection)
        coEvery {
            filterRuleRepository.addFilterRule(
                connection,
                rule,
                ruleType
            )
        } returns Result.success(response)

        val result = target(rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(response)
        coVerify { connectionRepository.fetchActive() }
        coVerify { filterRuleRepository.addFilterRule(connection, rule, ruleType) }
    }

    @Test
    fun `invoke - returns failure when connection fetch fails`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val exception = Exception("No active connection found")

        coEvery { connectionRepository.fetchActive() } returns Result.failure(exception)

        val result = target(rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { connectionRepository.fetchActive() }
        coVerify(exactly = 0) { filterRuleRepository.addFilterRule(any(), any(), any()) }
    }

    @Test
    fun `invoke - returns failure when adding filter rule fails`() = runTest {
        val rule = "example.com"
        val ruleType = FilterRuleTypeEntity.ALLOW
        val connection = ConnectionEntity.Version5.default
        val exception = Exception("Failed to add filter rule")

        coEvery { connectionRepository.fetchActive() } returns Result.success(connection)
        coEvery {
            filterRuleRepository.addFilterRule(
                connection,
                rule,
                ruleType
            )
        } returns Result.failure(exception)

        val result = target(rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify { connectionRepository.fetchActive() }
        coVerify { filterRuleRepository.addFilterRule(connection, rule, ruleType) }
    }
}
