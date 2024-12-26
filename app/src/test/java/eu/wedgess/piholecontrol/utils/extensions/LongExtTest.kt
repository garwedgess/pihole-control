package eu.wedgess.piholecontrol.utils.extensions

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LongExtTest {

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `toDateString - formats correctly with default zone`() {
        val epochSeconds: Long = 1672531200 // 2023-01-01 00:00:00 UTC
        val expected = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))

        val result = epochSeconds.toDateString()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDateString - formats correctly with specific zone`() {
        val epochSeconds: Long = 1672531200 // 2023-01-01 00:00:00 UTC
        val zoneId = ZoneId.of("America/New_York")
        val expected = LocalDateTime.of(2022, 12, 31, 19, 0, 0)
            .atZone(zoneId)
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))

        val result = epochSeconds.toDateString(zoneId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDateString - formats zero epoch correctly`() {
        val epochSeconds: Long = 0
        val expected = LocalDateTime.of(1970, 1, 1, 1, 0, 0)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))

        val result = epochSeconds.toDateString()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDateString - formats negative epoch correctly`() {
        val epochSeconds: Long = -1672531200 // Before 1970
        val expected = LocalDateTime.of(1917, 1, 1, 0, 0, 0)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))

        val result = epochSeconds.toDateString()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toEpochMillis - converts correctly with default zone`() {
        val localDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val expected: Long = 1672531200 // 2023-01-01 00:00:00 UTC
        val result = localDateTime.toEpochMillis()
        val expectedWithZone = localDateTime.atZone(ZoneId.systemDefault()).toInstant().epochSecond

        assertThat(result).isEqualTo(expectedWithZone)
    }

    @Test
    fun `toEpochMillis - converts correctly with specific zone`() {
        val localDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0)
        val zoneId = ZoneId.of("America/New_York")
        val expected: Long = 1672550400 // 2023-01-01 00:00:00 UTC
        val result = localDateTime.toEpochMillis(zoneId)
        val expectedWithZone = localDateTime.atZone(zoneId).toInstant().epochSecond

        assertThat(result).isEqualTo(expectedWithZone)
    }

    @Test
    fun `toEpochMillis - converts zero epoch correctly`() {
        val localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
        val expected: Long = 0
        val result = localDateTime.toEpochMillis()
        val expectedWithZone = localDateTime.atZone(ZoneId.systemDefault()).toInstant().epochSecond

        assertThat(result).isEqualTo(expectedWithZone)
    }

    @Test
    fun `toEpochMillis - converts negative epoch correctly`() {
        val localDateTime = LocalDateTime.of(1916, 11, 29, 0, 0, 0)
        val expected: Long = -1672531200
        val result = localDateTime.toEpochMillis()
        val expectedWithZone = localDateTime.atZone(ZoneId.systemDefault()).toInstant().epochSecond

        assertThat(result).isEqualTo(expectedWithZone)
    }
}
