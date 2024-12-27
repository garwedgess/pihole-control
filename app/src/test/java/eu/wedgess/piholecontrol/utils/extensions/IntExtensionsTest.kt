package eu.wedgess.piholecontrol.utils.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Locale

class IntExtensionsTest {

    @Test
    fun `formatWithThousands - formats large number correctly with default locale`() {
        val number = 1234567
        val formatted = number.formatWithThousands()
        val expected = "1,234,567"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun `formatWithThousands - formats zero correctly`() {
        val number = 0
        val formatted = number.formatWithThousands()
        assertThat(formatted).isEqualTo("0")
    }

    @Test
    fun `formatWithThousands - formats negative large number correctly with default locale`() {
        val number = -1234567
        val formatted = number.formatWithThousands()
        val expected = "-1,234,567"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun `formatWithThousands - formats number less than 1000 correctly`() {
        val number = 500
        val formatted = number.formatWithThousands()
        assertThat(formatted).isEqualTo("500")
    }

    @Test
    fun `formatWithThousands - formats number 1000 correctly`() {
        val number = 1000
        val formatted = number.formatWithThousands()
        val expected = "1,000"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun `formatWithThousands - formats negative number less than 1000 correctly`() {
        val number = -500
        val formatted = number.formatWithThousands()
        assertThat(formatted).isEqualTo("-500")
    }

    @Test
    fun `formatWithThousands - formats negative number 1000 correctly`() {
        val number = -1000
        val formatted = number.formatWithThousands()
        val expected = "-1,000"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun `formatWithThousands - formats correctly with US locale`() {
        val number = 1234567
        val formatted = number.formatWithThousands()
        if (Locale.getDefault() == Locale.US) {
            assertThat(formatted).isEqualTo("1,234,567")
        }
    }
}
