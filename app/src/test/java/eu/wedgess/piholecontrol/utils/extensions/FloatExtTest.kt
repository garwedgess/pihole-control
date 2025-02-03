package eu.wedgess.piholecontrol.utils.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FloatExtTest {

    @Test
    fun `GIVEN positive value WHEN format percentage THEN formats correctly`() {
        val value = 12.34f
        val expected = "12.34%"
        val actual = value.formatPercentage()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `GIVEN negative value WHEN format percentage THEN formats correctly`() {
        val value = -5.67f
        val expected = "-5.67%"
        val actual = value.formatPercentage()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `GIVEN zero value WHEN format percentage THEN formats correctly`() {
        val value = 0f
        val expected = "0.00%"
        val actual = value.formatPercentage()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `GIVEN large value WHEN format percentage THEN formats correctly`() {
        val value = 12345.67f
        val expected = "12,345.67%"
        val actual = value.formatPercentage()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `GIVEN zero degrees WHEN convert to radians THEN returns zero radians`() {
        val degrees = 0f
        val expected = 0f
        val actual = degrees.degreeToRadian
        assertThat(actual).isWithin(0.001f).of(expected)
    }

    @Test
    fun `GIVEN 90 degrees WHEN convert to radians THEN returns pi over 2 radians`() {
        val degrees = 90f
        val expected = (Math.PI / 2).toFloat()
        val actual = degrees.degreeToRadian
        assertThat(actual).isWithin(0.001f).of(expected)
    }

    @Test
    fun `GIVEN 180 degrees WHEN convert to radians THEN returns pi radians`() {
        val degrees = 180f
        val expected = Math.PI.toFloat()
        val actual = degrees.degreeToRadian
        assertThat(actual).isWithin(0.001f).of(expected)
    }

    @Test
    fun `GIVEN 360 degrees WHEN convert to radians THEN returns 2 pi radians`() {
        val degrees = 360f
        val expected = (2 * Math.PI).toFloat()
        val actual = degrees.degreeToRadian
        assertThat(actual).isWithin(0.001f).of(expected)
    }
}
