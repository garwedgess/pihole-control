package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.PiHoleStatusV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleStatusResponseDataV5
import eu.wedgess.piholecontrol.domain.model.StatusEntity
import org.junit.Test

class PiHoleSummaryStatusDataMapperTest {

    @Test
    fun `toPiHoleStatusResponse - maps ENABLED correctly`() {
        val result = StatusEntity.ENABLED.toPiHoleStatusResponse()
        assertThat(result.status).isEqualTo(PiHoleStatusV5.ENABLED)
    }

    @Test
    fun `toPiHoleStatusResponse - maps DISABLED correctly`() {
        val result = StatusEntity.DISABLED.toPiHoleStatusResponse()
        assertThat(result.status).isEqualTo(PiHoleStatusV5.DISABLED)
    }

    @Test
    fun `toPiHoleStatusResponse - maps UNKNOWN correctly`() {
        val result = StatusEntity.UNKNOWN.toPiHoleStatusResponse()
        assertThat(result.status).isEqualTo(PiHoleStatusV5.UNKNOWN)
    }

    @Test
    fun `toStatusEntity - maps ENABLED correctly`() {
        val response = PiHoleStatusResponseDataV5(PiHoleStatusV5.ENABLED)
        val result = response.toStatusEntity()
        assertThat(result).isEqualTo(StatusEntity.ENABLED)
    }

    @Test
    fun `toStatusEntity - maps DISABLED correctly`() {
        val response = PiHoleStatusResponseDataV5(PiHoleStatusV5.DISABLED)
        val result = response.toStatusEntity()
        assertThat(result).isEqualTo(StatusEntity.DISABLED)
    }

    @Test
    fun `toStatusEntity - maps UNKNOWN correctly`() {
        val response = PiHoleStatusResponseDataV5(PiHoleStatusV5.UNKNOWN)
        val result = response.toStatusEntity()
        assertThat(result).isEqualTo(StatusEntity.UNKNOWN)
    }
}
