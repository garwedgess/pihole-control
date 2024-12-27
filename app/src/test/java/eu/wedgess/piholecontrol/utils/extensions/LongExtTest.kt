package eu.wedgess.piholecontrol.utils.extensions

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

class LongExtTest {

    private val utcZoneId: ZoneId = ZoneOffset.UTC

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `toDateString - formats correctly with default zone`() {
        val epochSeconds: Long = 1672531200 // 2023-01-01 00:00:00 UTC
        val expected = "01/01/2023 - 00:00" // Expected in UTC

        val result = epochSeconds.toDateString(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDateString - formats zero epoch correctly`() {
        val epochSeconds: Long = 0
        val expected = "01/01/1970 - 00:00" // Expected in UTC

        val result = epochSeconds.toDateString(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDateString - formats negative epoch correctly`() {
        val epochSeconds: Long = -1672531200 // Before 1970
        val expected = "01/01/1917 - 00:00" // Expected in UTC

        val result = epochSeconds.toDateString(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toEpochSeconds - converts correctly with default zone`() {
        val localDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val expected: Long = 1672531200 // 2023-01-01 00:00:00 UTC

        val result = localDateTime.toEpochSeconds(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toEpochSeconds - converts zero epoch correctly`() {
        val localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
        val expected: Long = 0

        val result = localDateTime.toEpochSeconds(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toEpochSeconds - converts negative epoch correctly`() {
        val localDateTime = LocalDateTime.of(1916, 11, 29, 0, 0, 0)
        val expected: Long = -1675382400

        val result = localDateTime.toEpochSeconds(utcZoneId)

        assertThat(result).isEqualTo(expected)
    }
}
