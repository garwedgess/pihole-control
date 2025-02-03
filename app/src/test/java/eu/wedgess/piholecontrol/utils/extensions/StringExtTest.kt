package eu.wedgess.piholecontrol.utils.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class StringExtTest {

    @Test
    fun `GIVEN valid hostname WHEN check if valid host THEN returns true`() {
        val host = "www.example.com"
        assertThat(host.isValidHost()).isTrue()
    }

    @Test
    fun `GIVEN valid hostname with subdomain WHEN check if valid host THEN returns true`() {
        val host = "subdomain.example.co.uk"
        assertThat(host.isValidHost()).isTrue()
    }

    @Test
    fun `GIVEN valid IPv4 address WHEN check if valid host THEN returns true`() {
        val host = "192.168.1.1"
        assertThat(host.isValidHost()).isTrue()
    }

    @Test
    fun `GIVEN invalid hostname with special characters WHEN check if valid host THEN returns false`() {
        val host = "www.example!.com"
        assertThat(host.isValidHost()).isFalse()
    }

    @Test
    fun `GIVEN invalid hostname with spaces WHEN check if valid host THEN returns false`() {
        val host = "www. example.com"
        assertThat(host.isValidHost()).isFalse()
    }

    @Test
    fun `GIVEN invalid IPv4 address WHEN check if valid host THEN returns false`() {
        val host = "256.168.1.1"
        assertThat(host.isValidHost()).isFalse()
    }

    @Test
    fun `GIVEN empty string WHEN check if valid host THEN returns false`() {
        val host = ""
        assertThat(host.isValidHost()).isFalse()
    }

    @Test
    fun `GIVEN string with only digits WHEN check if digits only THEN returns true`() {
        val str = "12345"
        assertThat(str.isDigitsOnly()).isTrue()
    }

    @Test
    fun `GIVEN string with letters WHEN check if digits only THEN returns false`() {
        val str = "abc123"
        assertThat(str.isDigitsOnly()).isFalse()
    }

    @Test
    fun `GIVEN string with special characters WHEN check if digits only THEN returns false`() {
        val str = "123!@#\$"
        assertThat(str.isDigitsOnly()).isFalse()
    }

    @Test
    fun `GIVEN empty string WHEN check if digits only THEN returns false`() {
        val str = ""
        assertThat(str.isDigitsOnly()).isFalse()
    }
}
