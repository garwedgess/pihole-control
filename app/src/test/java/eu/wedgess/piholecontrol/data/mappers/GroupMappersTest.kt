package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.requests.PiHoleGroupRequestData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleGroupsResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyGroupResponseData
import eu.wedgess.piholecontrol.domain.model.GroupEntity
import eu.wedgess.piholecontrol.domain.model.ModifyGroupResponseEntity
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class GroupMappersTest {

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `toGroupEntity maps all fields correctly`() {
        // Arrange
        val groupData = PiHoleGroupsResponseData.GroupData(
            id = 5,
            name = "Test Group",
            enabled = true,
            comment = "Test comment",
            dateAdded = 1620000000L,
            dateModified = 1630000000L
        )

        // Calculate expected date strings for comparison
        val expectedDateAdded = Instant.ofEpochSecond(1620000000L)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .replace("T", " ")

        val expectedDateModified = Instant.ofEpochSecond(1630000000L)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .replace("T", " ")

        // Act
        val result = groupData.toGroupEntity()

        // Assert
        assertThat(result).isInstanceOf(GroupEntity::class.java)
        assertThat(result.id).isEqualTo(5)
        assertThat(result.name).isEqualTo("Test Group")
        assertThat(result.enabled).isTrue()
        assertThat(result.comment).isEqualTo("Test comment")
        assertThat(result.dateAdded).isEqualTo(expectedDateAdded)
        assertThat(result.dateModified).isEqualTo(expectedDateModified)
    }

    @Test
    fun `toPiHoleGroupRequestData maps all fields correctly`() {
        // Arrange
        val groupEntity = GroupEntity(
            id = 10,
            name = "Test Group",
            enabled = false,
            comment = "Test comment",
            dateAdded = "2021-05-03 12:00:00",
            dateModified = "2021-08-27 15:30:00"
        )

        // Act
        val result = groupEntity.toPiHoleGroupRequestData()

        // Assert
        assertThat(result).isInstanceOf(PiHoleGroupRequestData::class.java)
        assertThat(result.name).isEqualTo("Test Group")
        assertThat(result.enabled).isFalse()
        assertThat(result.comment).isEqualTo("Test comment")
        // Note: id, dateAdded and dateModified are not mapped to the request
    }

    @Test
    fun `toEntity maps success response correctly`() {
        // Arrange
        val successItem = PiHoleModifyGroupResponseData.SuccessItem(
            item = "Group successfully added"
        )
        val responseData = PiHoleModifyGroupResponseData(
            processed = PiHoleModifyGroupResponseData.Processed(
                success = listOf(successItem),
                errors = emptyList()
            ),
            groups = emptyList(),
            took = 0.123123123123,
        )

        // Act
        val result = responseData.toEntity()

        // Assert
        assertThat(result).isInstanceOf(ModifyGroupResponseEntity::class.java)
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("Group successfully added")
    }

    @Test
    fun `toEntity maps error response correctly`() {
        // Arrange
        val errorItem = PiHoleModifyGroupResponseData.ErrorItem(
            error = "Group name already exists",
            item = "test"
        )
        val responseData = PiHoleModifyGroupResponseData(
            processed = PiHoleModifyGroupResponseData.Processed(
                success = emptyList(),
                errors = listOf(errorItem)
            ),
            groups = emptyList(),
            took = 0.123123123123,
        )

        // Act
        val result = responseData.toEntity()

        // Assert
        assertThat(result).isInstanceOf(ModifyGroupResponseEntity::class.java)
        assertThat(result.success).isFalse()
        assertThat(result.message).isEqualTo(null) // Message should be null because of the logic in toEntity
    }

    @Test
    fun `toEntity handles null processed field`() {
        // Arrange
        val responseData = PiHoleModifyGroupResponseData(
            processed = null,
            groups = emptyList(),
            took = 0.123123123123,
        )

        // Act
        val result = responseData.toEntity()

        // Assert
        assertThat(result.success).isFalse()
        assertThat(result.message).isNull()
    }

    @Test
    fun `toEntity handles multiple success items`() {
        // Arrange
        val successItems = listOf(
            PiHoleModifyGroupResponseData.SuccessItem("First success"),
            PiHoleModifyGroupResponseData.SuccessItem("Second success")
        )
        val responseData = PiHoleModifyGroupResponseData(
            processed = PiHoleModifyGroupResponseData.Processed(
                success = successItems,
                errors = emptyList()
            ),
            groups = emptyList(),
            took = 0.123123123123,
        )

        // Act
        val result = responseData.toEntity()

        // Assert
        assertThat(result.success).isTrue()
        assertThat(result.message).isEqualTo("First success,Second success")
    }

    @Test
    fun `toEntity handles multiple error items`() {
        // Arrange
        val errorItems = listOf(
            PiHoleModifyGroupResponseData.ErrorItem("test1", "First error"),
            PiHoleModifyGroupResponseData.ErrorItem("test2", "Second error")
        )
        val responseData = PiHoleModifyGroupResponseData(
            processed = PiHoleModifyGroupResponseData.Processed(
                success = emptyList(),
                errors = errorItems
            ),
            groups = emptyList(),
            took = 0.123123123123,
        )

        // Act
        val result = responseData.toEntity()

        // Assert
        assertThat(result.success).isFalse()
        assertThat(result.message).isNull() // The logic seems to set message to null for errors
    }
}