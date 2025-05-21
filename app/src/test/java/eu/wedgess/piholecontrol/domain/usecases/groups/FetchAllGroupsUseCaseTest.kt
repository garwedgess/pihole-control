package eu.wedgess.piholecontrol.domain.usecases.groups

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.repository.ConnectionRepository
import eu.wedgess.piholecontrol.domain.repository.GroupRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchAllGroupsUseCaseTest {

    @MockK
    private lateinit var groupRepository: GroupRepository

    @MockK
    private lateinit var connectionRepository: ConnectionRepository

    private lateinit var target: FetchAllGroupsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        target = FetchAllGroupsUseCase(groupRepository, connectionRepository)
    }

    @Test
    fun `invoke - when active connection exists and groups fetch succeeds - should return success with groups`() =
        runTest {
            // Arrange
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val groups = listOf(
                mockk<GroupEntity>(relaxed = true),
                mockk<GroupEntity>(relaxed = true)
            )
            
            coEvery { connectionRepository.fetchActive() } returns Result.success(activeConnection)
            coEvery { groupRepository.fetchAllGroups(activeConnection) } returns Result.success(groups)

            // Act
            val result = target.invoke()

            // Assert
            assertThat(result.isSuccess).isTrue()
            val resultGroups = result.getOrNull()
            assertThat(resultGroups).isNotNull()
            assertThat(resultGroups).hasSize(2)
            assertThat(resultGroups).isEqualTo(groups)
            
            coVerify { connectionRepository.fetchActive() }
            coVerify { groupRepository.fetchAllGroups(activeConnection) }
        }

    @Test
    fun `invoke - when active connection exists but groups fetch fails - should return failure`() =
        runTest {
            // Arrange
            val activeConnection = mockk<ConnectionEntity>(relaxed = true)
            val exception = RuntimeException("Failed to fetch groups")
            
            coEvery { connectionRepository.fetchActive() } returns Result.success(activeConnection)
            coEvery { groupRepository.fetchAllGroups(activeConnection) } returns Result.failure(exception)

            // Act
            val result = target.invoke()

            // Assert
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            
            coVerify { connectionRepository.fetchActive() }
            coVerify { groupRepository.fetchAllGroups(activeConnection) }
        }

    @Test
    fun `invoke - when no active connection exists - should return failure`() =
        runTest {
            // Arrange
            val exception = RuntimeException("No active connection found")
            
            coEvery { connectionRepository.fetchActive() } returns Result.failure(exception)

            // Act
            val result = target.invoke()

            // Assert
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isEqualTo(exception)
            
            coVerify { connectionRepository.fetchActive() }
            coVerify(exactly = 0) { groupRepository.fetchAllGroups(any()) }
        }
}