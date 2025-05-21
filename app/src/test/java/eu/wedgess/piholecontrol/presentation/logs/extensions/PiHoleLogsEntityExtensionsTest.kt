package eu.wedgess.piholecontrol.presentation.logs.extensions

import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import eu.wedgess.piholecontrol.presentation.logs.model.LogEntryInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class PiHoleLogsEntityExtensionsTest {

    @Test
    fun `toInfo() should return correct LogEntryInfo`() {
        // Arrange
        val logEntity = PiHoleLogsEntity(
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
        val logEntryInfo = logEntity.toInfo()

        // Assert
        assertEquals(
            LogEntryInfo(
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
