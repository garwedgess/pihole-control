package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAuthSessionStatusResponseDataV6
import eu.wedgess.piholecontrol.domain.model.AuthSessionStatusEntity
import org.junit.Test

class AuthSessionStatusMapperTest {

    @Test
    fun `toEntity maps all fields correctly`() {
        // Arrange
        val response = PiHoleAuthSessionStatusResponseDataV6(
            session = PiHoleAuthSessionStatusResponseDataV6.PiHoleAuthSession(
                valid = true,
                totp = true,
                sid = "test-sid",
                csrf = "test-csrf", // This field is not mapped
                validity = 3600,
                message = "Success"
            ),
            took = 0.123 // This field is not mapped
        )

        // Act
        val result = response.toEntity()

        // Assert
        assertThat(result).isInstanceOf(AuthSessionStatusEntity::class.java)
        assertThat(result.valid).isEqualTo(true)
        assertThat(result.totp).isEqualTo(true)
        assertThat(result.sid).isEqualTo("test-sid")
        assertThat(result.validity).isEqualTo(3600)
        assertThat(result.message).isEqualTo("Success")
    }

    @Test
    fun `toEntity maps default values correctly`() {
        // Arrange
        val response = PiHoleAuthSessionStatusResponseDataV6()

        // Act
        val result = response.toEntity()

        // Assert
        assertThat(result.valid).isFalse()
        assertThat(result.totp).isFalse()
        assertThat(result.sid).isEmpty()
        assertThat(result.validity).isEqualTo(0)
        assertThat(result.message).isEmpty()
    }
}
