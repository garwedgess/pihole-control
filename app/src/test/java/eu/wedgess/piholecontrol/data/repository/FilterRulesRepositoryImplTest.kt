package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.v5.FilterRulesApiServiceV5
import eu.wedgess.piholecontrol.data.api.v6.FilterRulesApiServiceV6
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.mappers.toPiHoleFilterRuleTypeV6
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
    private lateinit var apiServiceV5: FilterRulesApiServiceV5

    @MockK
    private lateinit var apiServiceV6: FilterRulesApiServiceV6
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: FilterRulesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = FilterRulesRepositoryImpl(apiServiceV5, apiServiceV6, dispatcherProvider)
    }

    @Test
    fun `fetchFilterRules with V5 connection - should invoke V5 API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            coEvery {
                apiServiceV5.fetchCombinedFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleType()
                )
            } returns Result.success(mockk(relaxed = true))

            val result = target.fetchFilterRules(activeConnection, ruleType)

            assertThat(result.isSuccess).isTrue()
            val rules = result.getOrNull()
            assertThat(rules).isNotNull()
            coVerify {
                apiServiceV5.fetchCombinedFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleType()
                )
            }
        }

    @Test
    fun `fetchFilterRules with V5 connection - should invoke V5 API and return failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery {
                apiServiceV5.fetchCombinedFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleType()
                )
            } returns Result.failure(exception)

            val result = target.fetchFilterRules(activeConnection, ruleType)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify {
                apiServiceV5.fetchCombinedFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleType()
                )
            }
        }

    @Test
    fun `fetchFilterRules with V6 connection - should invoke V6 API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            coEvery {
                apiServiceV6.fetchFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            } returns Result.success(mockk(relaxed = true))

            val result = target.fetchFilterRules(activeConnection, ruleType)

            assertThat(result.isSuccess).isTrue()
            val rules = result.getOrNull()
            assertThat(rules).isNotNull()
            coVerify {
                apiServiceV6.fetchFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            }
        }

    @Test
    fun `fetchFilterRules with V6 connection - should invoke V6 API and return failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery {
                apiServiceV6.fetchFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            } returns Result.failure(exception)

            val result = target.fetchFilterRules(activeConnection, ruleType)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify {
                apiServiceV6.fetchFilterRules(
                    activeConnection,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            }
        }

    @Test
    fun `addFilterRule with V5 connection - should invoke V5 API and return success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        coEvery {
            apiServiceV5.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        } returns Result.success(mockk(relaxed = true))

        val result = target.addFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        val response = result.getOrNull()
        assertThat(response).isNotNull()
        coVerify {
            apiServiceV5.addFilterRule(
                activeConnection,
                rule,
                ruleType.toPiHoleFilterRuleType()
            )
        }
    }

    @Test
    fun `addFilterRule with V6 connection - should invoke V6 API and return success`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        coEvery {
            apiServiceV6.addFilterRule(
                eq(activeConnection),
                any(),
                ruleType.toPiHoleFilterRuleTypeV6()
            )
        } returns Result.success(mockk(relaxed = true))

        val result = target.addFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isSuccess).isTrue()
        val response = result.getOrNull()
        assertThat(response).isNotNull()
        coVerify {
            apiServiceV6.addFilterRule(
                eq(activeConnection),
                any(),
                ruleType.toPiHoleFilterRuleTypeV6()
            )
        }
    }

    @Test
    fun `addFilterRule with V6 connection - should invoke V6 API and return failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
        val rule = "testRule"
        val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery {
            apiServiceV6.addFilterRule(
                eq(activeConnection),
                any(),
                ruleType.toPiHoleFilterRuleTypeV6()
            )
        } returns Result.failure(exception)

        val result = target.addFilterRule(activeConnection, rule, ruleType)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify {
            apiServiceV6.addFilterRule(
                eq(activeConnection),
                any(),
                ruleType.toPiHoleFilterRuleTypeV6()
            )
        }
    }

    @Test
    fun `removeFilterRule with V5 connection - should invoke V5 API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version5>(relaxed = true)
            val rule = "testRule"
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            coEvery {
                apiServiceV5.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleType()
                )
            } returns Result.success(mockk(relaxed = true))

            val result = target.removeFilterRule(activeConnection, rule, ruleType)

            assertThat(result.isSuccess).isTrue()
            val response = result.getOrNull()
            assertThat(response).isNotNull()
            coVerify {
                apiServiceV5.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleType()
                )
            }
        }

    @Test
    fun `removeFilterRule with V6 connection - should invoke V6 API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val rule = "testRule"
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            coEvery {
                apiServiceV6.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            } returns Result.success(Unit)

            val result = target.removeFilterRule(activeConnection, rule, ruleType)

            assertThat(result.isSuccess).isTrue()
            val response = result.getOrNull()
            assertThat(response).isNotNull()
            coVerify {
                apiServiceV6.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            }
        }

    @Test
    fun `removeFilterRule with V6 connection - should invoke V6 API and return failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity.Version6>(relaxed = true)
            val rule = "testRule"
            val ruleType = mockk<FilterRuleTypeEntity>(relaxed = true)
            val exception = RuntimeException("API error")
            coEvery {
                apiServiceV6.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            } returns Result.failure(exception)

            val result = target.removeFilterRule(activeConnection, rule, ruleType)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify {
                apiServiceV6.removeFilterRule(
                    activeConnection,
                    rule,
                    ruleType.toPiHoleFilterRuleTypeV6()
                )
            }
        }
}
