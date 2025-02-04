package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.PiHoleApiVersionData
import eu.wedgess.piholecontrol.domain.model.PiHoleApiVersionEntity
import org.junit.Test

class PiHoleApiVersionMapperTest {

    @Test
    fun `when mapping Version5 PiHoleApiVersionData to Entity, then correct entity is returned`() {
        // Given
        val version5Data = PiHoleApiVersionData.Version5

        // When
        val version5Entity = version5Data.toEntity()

        // Then
        assertThat(version5Entity).isEqualTo(PiHoleApiVersionEntity.Version5)
    }

    @Test
    fun `when mapping Version6 PiHoleApiVersionData to Entity, then correct entity is returned`() {
        // Given
        val version6Data = PiHoleApiVersionData.Version6

        // When
        val version6Entity = version6Data.toEntity()

        // Then
        assertThat(version6Entity).isEqualTo(PiHoleApiVersionEntity.Version6)
    }

    @Test
    fun `when mapping Version5 PiHoleApiVersionEntity to Data, then correct data is returned`() {
        // Given
        val version5Entity = PiHoleApiVersionEntity.Version5

        // When
        val version5Data = version5Entity.fromEntity()

        // Then
        assertThat(version5Data).isEqualTo(PiHoleApiVersionData.Version5)
    }

    @Test
    fun `when mapping Version6 PiHoleApiVersionEntity to Data, then correct data is returned`() {
        // Given
        val version6Entity = PiHoleApiVersionEntity.Version6

        // When
        val version6Data = version6Entity.fromEntity()

        // Then
        assertThat(version6Data).isEqualTo(PiHoleApiVersionData.Version6)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when mapping invalid key in PiHoleApiVersionData, then throws IllegalArgumentException`() {
        // When
        PiHoleApiVersionData[999L].toEntity()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `when mapping invalid key in PiHoleApiVersionEntity, then throws IllegalArgumentException`() {
        // When
        PiHoleApiVersionEntity[999L].fromEntity()
    }
}
