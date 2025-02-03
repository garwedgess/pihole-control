package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleOverTimeResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleOverTimeResponseDataV6
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
    fun `toQueriesOverTimeData - maps PiHoleOverTimeData to QueriesOverTimeEntity correctly`() {
        val piHoleOverTimeResponseDataV5 = PiHoleOverTimeResponseDataV5(
            domainsOverTime = mapOf(
                16788864L to 10L,
                16788900L to 20L
            ),
            adsOverTime = mapOf(
                16788864L to 5L,
                16788900L to 8L
            )
        )

        val queriesOverTimeEntity = piHoleOverTimeResponseDataV5.toEntity()

        assertThat(queriesOverTimeEntity.permitted).hasSize(2)
        assertThat(queriesOverTimeEntity.blocked).hasSize(2)

        assertThat(queriesOverTimeEntity.permitted[0].timestamp).isEqualTo(16788864000L)
        assertThat(queriesOverTimeEntity.permitted[0].hits).isEqualTo(10L)
        assertThat(queriesOverTimeEntity.permitted[0].time).isEqualTo("13:20")

        assertThat(queriesOverTimeEntity.permitted[1].timestamp).isEqualTo(16788900000L)
        assertThat(queriesOverTimeEntity.permitted[1].hits).isEqualTo(20L)
        assertThat(queriesOverTimeEntity.permitted[1].time).isEqualTo("23:20")

        assertThat(queriesOverTimeEntity.blocked[0].timestamp).isEqualTo(16788864000L)
        assertThat(queriesOverTimeEntity.blocked[0].hits).isEqualTo(5L)
        assertThat(queriesOverTimeEntity.blocked[0].time).isEqualTo("13:20")

        assertThat(queriesOverTimeEntity.blocked[1].timestamp).isEqualTo(16788900000L)
        assertThat(queriesOverTimeEntity.blocked[1].hits).isEqualTo(8L)
        assertThat(queriesOverTimeEntity.blocked[1].time).isEqualTo("23:20")
    }

    @Test
    fun `toQueriesOverTimeData - maps empty PiHoleOverTimeData`() {
        val piHoleOverTimeResponseDataV5 = PiHoleOverTimeResponseDataV5()

        val queriesOverTimeEntity = piHoleOverTimeResponseDataV5.toEntity()

        assertThat(queriesOverTimeEntity.permitted).isEmpty()
        assertThat(queriesOverTimeEntity.blocked).isEmpty()
    }

    @Test
    fun `OverTimeEntity - time property formats correctly`() {
        val overTimeEntity = OverTimeEntity(
            timestamp = 16788864000L,
            hits = 10
        )

        assertThat(overTimeEntity.time).isEqualTo("13:20")
    }

    @Test
    fun `GIVEN PiHoleOverTimeResponseDataV5 WHEN toEntity THEN maps to QueriesOverTimeEntity correctly`() {
        val responseV5 = PiHoleOverTimeResponseDataV5(
            domainsOverTime = mapOf(16788864L to 10L, 16788900L to 20L),
            adsOverTime = mapOf(16788864L to 5L, 16788900L to 8L)
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

        val actualEntity = responseV5.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN PiHoleOverTimeResponseDataV6 WHEN toEntity THEN maps to QueriesOverTimeEntity correctly`() {
        val responseV6 = PiHoleOverTimeResponseDataV6(
            history = listOf(
                PiHoleOverTimeResponseDataV6.PiHoleOverTimeHistoryData(
                    timestamp = 16788864,
                    total = 15,
                    cached = 10,
                    blocked = 5,
                    forwarded = 0
                ),
                PiHoleOverTimeResponseDataV6.PiHoleOverTimeHistoryData(
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
