package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleLogSuggestionsResponseData
import eu.wedgess.piholecontrol.domain.model.LogFilterSuggestionsEntity
import org.junit.Test

class LogFilterSuggestionsMapperTest {

    @Test
    fun `GIVEN PiHoleLogSuggestionsResponseDataV6 WHEN toEntity THEN maps to LogFilterSuggestionsEntity correctly`() {
        val responseV6 = PiHoleLogSuggestionsResponseData(
            suggestions = PiHoleLogSuggestionsResponseData.PiHoleSuggestionData(
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
