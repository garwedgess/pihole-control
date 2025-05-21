package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleOverTimeResponseData
import eu.wedgess.piholecontrol.domain.model.OverTimeEntity
import eu.wedgess.piholecontrol.domain.model.QueriesOverTimeEntity
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test

class QueriesOverTimeMapperTest {

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `GIVEN PiHoleOverTimeResponseDataV6 WHEN toEntity THEN maps to QueriesOverTimeEntity correctly`() {
        val responseV6 = PiHoleOverTimeResponseData(
            history = listOf(
                PiHoleOverTimeResponseData.PiHoleOverTimeHistoryData(
                    timestamp = 16788864,
                    total = 15,
                    cached = 10,
                    blocked = 5,
                    forwarded = 0
                ),
                PiHoleOverTimeResponseData.PiHoleOverTimeHistoryData(
                    timestamp = 16788900,
                    total = 28,
                    cached = 20,
                    blocked = 8,
                    forwarded = 0
                )
            )
        )
        val expectedEntity = QueriesOverTimeEntity(
            permitted = listOf(
                OverTimeEntity(16788864000L, 10L),
                OverTimeEntity(16788900000L, 20L)
            ),
            blocked = listOf(
                OverTimeEntity(16788864000L, 5L),
                OverTimeEntity(16788900000L, 8L)
            )
        )

        val actualEntity = responseV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }
}
