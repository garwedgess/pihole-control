package eu.wedgess.piholecontrol.data.repository

import TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.api.GroupApiService
import eu.wedgess.piholecontrol.data.mappers.toEntity
import eu.wedgess.piholecontrol.data.mappers.toGroupEntity
import eu.wedgess.piholecontrol.data.mappers.toPiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleGroupsResponseData
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.repository.GroupRepository
import eu.wedgess.piholecontrol.initThreeTen
import eu.wedgess.piholecontrol.utils.DispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GroupRepositoryImplTest {

    @MockK
    private lateinit var apiService: GroupApiService
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var target: GroupRepository

    @Before
    fun setUp() {
        initThreeTen()
        MockKAnnotations.init(this)
        dispatcherProvider = TestDispatcherProvider()
        target = GroupRepositoryImpl(apiService, dispatcherProvider)
    }

    @Test
    fun `fetchAllGroups with connection - should invoke API and return success`() =
        runTest {
            val activeConnection = ConnectionEntity.default
            val mockResponse = PiHoleGroupsResponseData(
                groups = listOf(
                    PiHoleGroupsResponseData.GroupData(
                        name = "Test",
                        comment = "comment",
                        enabled = true,
                        id = 1,
                        dateAdded = 12324324211L,
                        dateModified = 12324324211L,
                    )
                )
            )
            coEvery {
                apiService.fetchAllGroups(activeConnection)
            } returns Result.success(mockResponse)

            val result = target.fetchAllGroups(activeConnection)

            assertThat(result.isSuccess).isTrue()
            val groups = result.getOrNull()
            assertThat(groups).isNotNull()
            assertThat(groups).hasSize(1)
            coVerify {
                apiService.fetchAllGroups(activeConnection)
            }
        }

    @Test
    fun `fetchAllGroups with connection - should invoke API and return failure`() =
        runTest {
            val activeConnection = ConnectionEntity.default
            val exception = RuntimeException("API error")
            coEvery {
                apiService.fetchAllGroups(activeConnection)
            } returns Result.failure(exception)

            val result = target.fetchAllGroups(activeConnection)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify {
                apiService.fetchAllGroups(activeConnection)
            }
        }

    @Test
    fun `updateGroup with connection - should invoke API and return success`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val name = "TestGroup"
        val group = mockk<GroupEntity>(relaxed = true)
        coEvery {
            apiService.updateGroup(
                eq(activeConnection),
                eq(name),
                any()
            )
        } returns Result.success(mockk(relaxed = true))

        val result = target.updateGroup(
            connection = activeConnection,
            name = name,
            group = group
        )

        assertThat(result.isSuccess).isTrue()
        val response = result.getOrNull()
        assertThat(response).isNotNull()
        coVerify {
            apiService.updateGroup(
                eq(activeConnection),
                eq(name),
                group.toPiHoleGroupRequestData()
            )
        }
    }

    @Test
    fun `updateGroup with connection - should invoke API and return failure`() = runTest {
        val activeConnection = mockk<ConnectionEntity>(relaxed = true)
        val name = "TestGroup"
        val group = mockk<GroupEntity>(relaxed = true)
        val exception = RuntimeException("API error")
        coEvery {
            apiService.updateGroup(
                eq(activeConnection),
                eq(name),
                any()
            )
        } returns Result.failure(exception)

        val result = target.updateGroup(
            connection = activeConnection,
            name = name,
            group = group
        )

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
        coVerify {
            apiService.updateGroup(
                eq(activeConnection),
                eq(name),
                group.toPiHoleGroupRequestData()
            )
        }
    }

    @Test
    fun `deleteGroup with connection - should invoke API and return success`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val name = "TestGroup"
            coEvery {
                apiService.deleteGroup(
                    activeConnection,
                    name
                )
            } returns Result.success(Unit)

            val result = target.deleteGroup(activeConnection, name)

            assertThat(result.isSuccess).isTrue()
            val response = result.getOrNull()
            assertThat(response).isNotNull()
            coVerify {
                apiService.deleteGroup(
                    activeConnection,
                    name
                )
            }
        }

    @Test
    fun `deleteGroup with connection - should invoke API and return failure`() =
        runTest {
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val name = "TestGroup"
            val exception = RuntimeException("API error")
            coEvery {
                apiService.deleteGroup(
                    activeConnection,
                    name
                )
            } returns Result.failure(exception)

            val result = target.deleteGroup(activeConnection, name)

            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            coVerify {
                apiService.deleteGroup(
                    activeConnection,
                    name
                )
            }
        }
}