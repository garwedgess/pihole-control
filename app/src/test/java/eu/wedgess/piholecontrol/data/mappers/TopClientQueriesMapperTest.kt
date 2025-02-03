package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.TopClientData
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleTopClientsResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleTopClientsCombinedResponseDataV6
import eu.wedgess.piholecontrol.domain.model.TopClientEntity
import eu.wedgess.piholecontrol.domain.model.TopClientQueriesEntity
import org.junit.Test

class TopClientQueriesMapperTest {

    @Test
    fun `GIVEN PiHoleTopClientsResponseDataV5 WHEN toEntity THEN maps to TopClientQueriesEntity correctly AND applies sorting`() {
        val responseV5 = PiHoleTopClientsResponseDataV5(
            topSources = mapOf("client1" to 10, "client2" to 5),
            topSourcesBlocked = mapOf("client1" to 2, "client3" to 3)
        )
        val expectedEntity = TopClientQueriesEntity(
            all = listOf(
                TopClientEntity("client1", 10, 66.66f),
                TopClientEntity("client2", 5, 33.33f)
            ),
            blocked = listOf(
                TopClientEntity("client3", 3, 60f),
                TopClientEntity("client1", 2, 40f),
            )
        )

        val actualEntity = responseV5.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN PiHoleTopClientsCombinedResponseDataV6 WHEN toEntity THEN maps to TopClientQueriesEntity correctly AND applies sorting`() {
        val responseV6 = PiHoleTopClientsCombinedResponseDataV6(
            all = listOf(
                PiHoleTopClientDataV6("client1", "192.126.1.4", 10),
                PiHoleTopClientDataV6("client2", "192.126.1.5", 5)
            ),
            blocked = listOf(
                PiHoleTopClientDataV6("client1", "192.126.1.2", 2),
                PiHoleTopClientDataV6("client3", "192.126.1.3", 3)
            )
        )
        val expectedEntity = TopClientQueriesEntity(
            all = listOf(
                TopClientEntity("client1|192.126.1.4", 10, 66.66f),
                TopClientEntity("client2|192.126.1.5", 5, 33.33f)
            ),
            blocked = listOf(
                TopClientEntity("client3|192.126.1.3", 3, 60f),
                TopClientEntity("client1|192.126.1.2", 2, 40f)
            )
        )

        val actualEntity = responseV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN TopClientData WHEN toEntity THEN maps to TopClientEntity correctly`() {
        val actualData = TopClientData("client1|192.126.1.4", 10, 66.66f)
        val expectedEntity = TopClientEntity("client1|192.126.1.4", 10, 66.66f)

        val actualEntity = actualData.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }
}
