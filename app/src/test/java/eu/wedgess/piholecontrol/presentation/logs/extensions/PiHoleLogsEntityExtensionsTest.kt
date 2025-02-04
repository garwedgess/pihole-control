package eu.wedgess.piholecontrol.presentation.logs.extensions

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class PiHoleLogsEntityExtensionsTest {

    @Test
    fun `toInfo() should return correct LogEntryInfo for Version5`() {
        // Arrange
        val version5Log = PiHoleLogsEntity.Version5(
            queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.SOA,
            client = "client",
            domain = "domain",
            replyTime = 10.0,
            answerType = PiHoleLogsEntity.LogsAnswerTypeEntity.UPSTREAM,
            timestamp = 123456789,
            time = "10:00"
        )

        // Act
        val logEntryInfo = version5Log.toInfo()

        // Assert
        assertThat(
            LogEntryInfo.Version5(
                queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.SOA,
                client = "client",
                domain = "domain",
                replyTime = 10.0,
                answerType = PiHoleLogsEntity.LogsAnswerTypeEntity.UPSTREAM,
                timestamp = 123456789,
                time = "10:00"
            )
        ).isEqualTo(logEntryInfo)
    }

    @Test
    fun `toInfo() should return correct LogEntryInfo for Version6`() {
        // Arrange
        val version6Log = PiHoleLogsEntity.Version6(
            timestamp = 987654321,
            client = "client",
            domain = "domain",
            id = 123,
            queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.PTR,
            status = PiHoleLogsEntity.LogEntryStatusEntity.CACHE,
            dnssec = PiHoleLogsEntity.LogEntryDnssecEntity.UNKNOWN,
            replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity.DOMAIN,
            replyTime = 20.0,
            listId = 456,
            edeCode = 789,
            edeText = "edeText",
            cname = "cname",
            time = "11:00"
        )

        // Act
        val logEntryInfo = version6Log.toInfo()

        // Assert
        assertEquals(
            LogEntryInfo.Version6(
                timestamp = 987654321,
                client = "client",
                domain = "domain",
                id = 123,
                queryType = PiHoleLogsEntity.LogEntryQueryTypeEntity.PTR,
                status = PiHoleLogsEntity.LogEntryStatusEntity.CACHE,
                dnssec = PiHoleLogsEntity.LogEntryDnssecEntity.UNKNOWN,
                replyType = PiHoleLogsEntity.LogEntryReplyTypeEntity.DOMAIN,
                replyTime = 20.0,
                listId = 456,
                edeCode = 789,
                edeText = "edeText",
                cname = "cname",
                time = "11:00"
            ),
            logEntryInfo
        )
    }
}
