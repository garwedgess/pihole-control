package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleClientsOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleLogSuggestionsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleLogSuggestionsResponseDataV6
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import eu.wedgess.piholecontrol.domain.model.PiHoleLogsEntity
import org.junit.Test

class LogFilterSuggestionsMapperTest {

    @Test
    fun `GIVEN PiHoleLogSuggestionsResponseDataV5 WHEN toEntity THEN maps to LogFilterSuggestionsEntity correctly`() {
        val responseV5 = PiHoleLogSuggestionsResponseDataV5(
            clients = listOf(
                PiHoleClientsOverTimeResponseDataV5.PiHoleClientsOverTimeClientData(
                    "client1",
                    "1.1.1.1"
                ),
                PiHoleClientsOverTimeResponseDataV5.PiHoleClientsOverTimeClientData(
                    "client2",
                    "2.2.2.2"
                )
            )
        )
        val expectedEntity = LogFilterSuggestionsEntity(
            clientNames = listOf(DEFAULT_SUGGESTION_ENTRY, "client1", "client2"),
            clientIpAddresses = listOf(DEFAULT_SUGGESTION_ENTRY, "1.1.1.1", "2.2.2.2"),
            statuses = buildList {
                add(DEFAULT_SUGGESTION_ENTRY)
                addAll(PiHoleLogsEntity.LogsAnswerTypeEntity.entries.map { it.key })
            },
            queryTypes = buildList {
                add(DEFAULT_SUGGESTION_ENTRY)
                addAll(PiHoleLogsEntity.LogEntryQueryTypeEntity.entries.map { it.key })
            },
            domains = emptyList(),
            upstreams = emptyList(),
            replyTypes = emptyList(),
            dnsSecs = emptyList()
        )

        val actualEntity = responseV5.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN PiHoleLogSuggestionsResponseDataV6 WHEN toEntity THEN maps to LogFilterSuggestionsEntity correctly`() {
        val responseV6 = PiHoleLogSuggestionsResponseDataV6(
            suggestions = PiHoleLogSuggestionsResponseDataV6.PiHoleSuggestionData(
                domains = listOf("domain1", "domain2"),
                clientIpAddresses = listOf("1.1.1.1", "2.2.2.2"),
                clientNames = listOf("client1", "client2"),
                upstreams = listOf("upstream1", "upstream2"),
                queryTypes = listOf("queryType1", "queryType2"),
                statuses = listOf("status1", "status2"),
                replyTypes = listOf("replyType1", "replyType2"),
                dnsSecs = listOf("dnsSec1", "dnsSec2")
            )
        )
        val expectedEntity = LogFilterSuggestionsEntity(
            clientNames = listOf(DEFAULT_SUGGESTION_ENTRY, "client1", "client2"),
            clientIpAddresses = listOf(DEFAULT_SUGGESTION_ENTRY, "1.1.1.1", "2.2.2.2"),
            statuses = listOf(DEFAULT_SUGGESTION_ENTRY, "status1", "status2"),
            queryTypes = listOf(DEFAULT_SUGGESTION_ENTRY, "queryType1", "queryType2"),
            domains = listOf("domain1", "domain2"),
            upstreams = listOf("upstream1", "upstream2"),
            replyTypes = listOf("replyType1", "replyType2"),
            dnsSecs = listOf("dnsSec1", "dnsSec2")
        )

        val actualEntity = responseV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }
}
