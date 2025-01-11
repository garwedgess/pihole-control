package eu.wedgess.piholecontrol.domain.usecases.filters

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleEntity
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.domain.usecases.PeriodicRefreshUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchFilterRulesUseCaseTest {

    @MockK
    private lateinit var fetchFilterRuleUseCase: FetchFilterRuleUseCase

    @MockK
    private lateinit var periodicRefreshUseCase: PeriodicRefreshUseCase
    private lateinit var target: FetchFilterRulesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchFilterRulesUseCase(fetchFilterRuleUseCase, periodicRefreshUseCase)
    }

    @Test
    fun `invoke - returns combined filter and regex filter rules`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val ruleType = FilterRuleTypeEntity.ALLOW
        val rules = listOf<FilterRuleEntity>(mockk(relaxed = true), mockk(relaxed = true))

        coEvery {
            fetchFilterRuleUseCase(
                connection,
                FilterRuleTypeEntity.ALLOW
            )
        } returns Result.success(rules)
        coEvery { periodicRefreshUseCase<List<FilterRuleEntity>>(any()) } answers {
            flow {
                val fetchBlock =
                    arg<suspend (ConnectionEntity) -> Result<List<FilterRuleEntity>>>(0)
                emit(fetchBlock(connection))
            }
        }

        target(ruleType).test {
            val result = awaitItem()
            assertThat(result.isSuccess).isTrue()
            val data = result.getOrDefault(emptyList())
            assertThat(data).isEqualTo(rules)
            awaitComplete()
        }
    }

    @Test
    fun `invoke - returns failure when exception occurs`() = runTest {
        val connection = ConnectionEntity.Version5.default
        val ruleType = FilterRuleTypeEntity.DENY
        val regexRulesException = Exception("Failed to fetch regex rules")

        coEvery { fetchFilterRuleUseCase(connection, FilterRuleTypeEntity.DENY) } returns
                Result.failure(regexRulesException)
        coEvery { periodicRefreshUseCase<List<FilterRuleEntity>>(any()) } answers {
            flow {
                val fetchBlock =
                    arg<suspend (ConnectionEntity) -> Result<List<FilterRuleEntity>>>(0)
                emit(fetchBlock(connection))
            }
        }

        target(ruleType).test {
            val result = awaitItem()
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(regexRulesException)
            awaitComplete()
        }
    }

    @Test
    fun `refreshRules - triggers refresh on periodicRefreshUseCase`() {
        every { periodicRefreshUseCase.triggerRefresh() } returns true

        target.refreshRules()

        verify { periodicRefreshUseCase.triggerRefresh() }
    }
}
