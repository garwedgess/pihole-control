package eu.wedgess.piholecontrol.data.utils.serializers

import androidx.datastore.core.CorruptionException
import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.UserPreferences
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class UserPreferencesSerializerTest {

    @Test
    fun `defaultValue should be default values`() {
        // Arrange
        val expectedDefault = UserPreferences.newBuilder()
            .setThemeValue(UserPreferences.Theme.DARK_VALUE)
            .setDefaultLogEntries(500)
            .setUseDynamicColors(false)
            .setRefreshTime(10_000)
            .setChangeStatusOnAllConnection(false)
            .build()

        // Act
        val actualDefault = UserPreferencesSerializer.defaultValue

        // Assert
        assertEquals(expectedDefault, actualDefault)
    }

    @Test
    fun `readFrom() should return UserPreferences when input is valid`() = runTest {
        // Arrange
        val expectedUserPreferences = UserPreferences.newBuilder()
            .setThemeValue(UserPreferences.Theme.LIGHT_VALUE)
            .setDefaultLogEntries(100)
            .setUseDynamicColors(true)
            .setRefreshTime(5_000)
            .setChangeStatusOnAllConnection(true)
            .build()
        val outputStream = ByteArrayOutputStream()
        expectedUserPreferences.writeTo(outputStream)
        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        // Act
        val actualUserPreferences = UserPreferencesSerializer.readFrom(inputStream)

        // Assert
        assertEquals(expectedUserPreferences, actualUserPreferences)
    }

    @Test
    fun `readFrom() should throw CorruptionException when input is invalid`() = runTest {
        val invalidInputStream = ByteArrayInputStream("invalid".toByteArray())

        // Act & Assert
        val exception = runCatching {
            UserPreferencesSerializer.readFrom(invalidInputStream)
        }.exceptionOrNull()
        assertThat(exception).isInstanceOf(CorruptionException::class.java)
        assertThat(exception).hasMessageThat().contains("Cannot read proto.")
    }

    @Test
    fun `writeTo() should write UserPreferences to output stream`() = runTest {
        // Arrange
        val userPreferences = UserPreferences.newBuilder()
            .setThemeValue(UserPreferences.Theme.LIGHT_VALUE)
            .setDefaultLogEntries(100)
            .setUseDynamicColors(true)
            .setRefreshTime(5_000)
            .setChangeStatusOnAllConnection(true)
            .build()
        val outputStream = ByteArrayOutputStream()

        // Act
        UserPreferencesSerializer.writeTo(userPreferences, outputStream)

        // Assert
        assertEquals(userPreferences, UserPreferences.parseFrom(outputStream.toByteArray()))
    }

    @Test
    fun `writeTo() should throw exception when output stream is invalid`() = runTest {
        // Arrange
        val userPreferences = UserPreferences.newBuilder()
            .setThemeValue(UserPreferences.Theme.LIGHT_VALUE)
            .setDefaultLogEntries(100)
            .setUseDynamicColors(true)
            .setRefreshTime(5_000)
            .setChangeStatusOnAllConnection(true)
            .build()

        val invalidOutputStream = object : OutputStream() {
            override fun write(b: Int): Unit = throw Exception()
            override fun write(b: ByteArray): Unit = throw Exception()
            override fun write(b: ByteArray, off: Int, len: Int): Unit = throw Exception()

            override fun flush(): Unit = throw Exception()
            override fun close(): Unit = throw Exception()
        }

        // Act & Assert
        val exception = runCatching {
            UserPreferencesSerializer.writeTo(userPreferences, invalidOutputStream)
        }.exceptionOrNull()
        assertThat(exception).isInstanceOf(Exception::class.java)
    }
}
