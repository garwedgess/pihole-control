package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLog
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LogEntryMapperTest {

    @Test
    fun `toLogEntryEntity - maps PiHoleLog to LogEntryEntity correctly`() {
        val piHoleLog = PiHoleLog(
            timestamp = 1678886400, // March 15, 2023 00:00:00 UTC
            queryType = "A",
            requestedDomain = "example.com",
            client = "192.168.1.10",
            answerType = LogsAnswerType.UPSTREAM,
            responseTime = 150
        )

        val logEntryEntity = piHoleLog.toLogEntryEntity()

        assertThat(logEntryEntity.timestamp).isEqualTo(1678886400)
        assertThat(logEntryEntity.queryType).isEqualTo("A")
        assertThat(logEntryEntity.requestedDomain).isEqualTo("example.com")
        assertThat(logEntryEntity.client).isEqualTo("192.168.1.10")
        assertThat(logEntryEntity.answerType).isEqualTo(LogAnswerTypeEntity.UPSTREAM)
        assertThat(logEntryEntity.responseTime).isEqualTo(150)
        assertThat(logEntryEntity.time).isEqualTo(
            Instant.ofEpochSecond(1678886400)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_TIME)
        )
    }

    @Test
    fun `toLogEntryEntity - maps PiHoleLog with different answer type`() {
        val piHoleLog = PiHoleLog(
            timestamp = 1678886400, // March 15, 2023 00:00:00 UTC
            queryType = "AAAA",
            requestedDomain = "example.net",
            client = "192.168.1.20",
            answerType = LogsAnswerType.GRAVITY_BLOCK,
            responseTime = 50
        )

        val logEntryEntity = piHoleLog.toLogEntryEntity()

        assertThat(logEntryEntity.answerType).isEqualTo(LogAnswerTypeEntity.GRAVITY_BLOCK)
    }

    @Test
    fun `toLogEntryEntity - maps PiHoleLog with different timestamp`() {
        val piHoleLog = PiHoleLog(
            timestamp = 1678972800, // March 16, 2023 00:00:00 UTC
            queryType = "AAAA",
            requestedDomain = "example.net",
            client = "192.168.1.20",
            answerType = LogsAnswerType.GRAVITY_BLOCK,
            responseTime = 50
        )

        val logEntryEntity = piHoleLog.toLogEntryEntity()

        assertThat(logEntryEntity.time).isEqualTo(
            Instant.ofEpochSecond(1678972800)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_TIME)
        )
    }
}
