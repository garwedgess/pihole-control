package eu.wedgess.piholecontrol.presentation.connections.modify.extensions

import eu.wedgess.piholecontrol.domain.model.PiHoleApiVersionEntity
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class PiHoleApiVersionExtTest {

    @Test
    fun `toEntity() should return correct PiHoleApiVersionEntity`() {
        // Arrange
        val version5 = PiHoleApiVersion.Version5
        val version6 = PiHoleApiVersion.Version6

        // Act
        val entity5 = version5.toEntity()
        val entity6 = version6.toEntity()

        // Assert
        assertEquals(PiHoleApiVersionEntity.Version5, entity5)
        assertEquals(PiHoleApiVersionEntity.Version6, entity6)
    }

    @Test
    fun `fromEntity() should return correct PiHoleApiVersion`() {
        // Arrange
        val entity5 = PiHoleApiVersionEntity.Version5
        val entity6 = PiHoleApiVersionEntity.Version6

        // Act
        val version5 = entity5.fromEntity()
        val version6 = entity6.fromEntity()

        // Assert
        assertEquals(PiHoleApiVersion.Version5, version5)
        assertEquals(PiHoleApiVersion.Version6, version6)
    }

    @Test
    fun `toEntity() should throw exception for invalid key`() {
        // Arrange
        val invalidKey = 99L

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            val invalidVersion = PiHoleApiVersion[invalidKey]
            invalidVersion.toEntity()
        }
    }

    @Test
    fun `fromEntity() should throw exception for invalid key`() {
        // Arrange
        val invalidKey = 99L

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            val invalidEntity = PiHoleApiVersionEntity[invalidKey]
            invalidEntity.fromEntity()
        }
    }
}
