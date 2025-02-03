package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.UpstreamDestinationsWithPercentagesV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamStatisticsV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleUpstreamV6Data
import eu.wedgess.piholecontrol.data.model.responses.v6.UpstreamDestinationsWithPercentagesV6
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity
import org.junit.Test

class UpstreamDestinationsWithPercentagesMapperTest {

    @Test
    fun `GIVEN UpstreamDestinationsWithPercentagesV5 WHEN toEntityList THEN maps to list of UpstreamDestinationEntity correctly`() {
        val upstreamDestinationsV5: UpstreamDestinationsWithPercentagesV5 = mapOf(
            "destination1" to 10f,
            "destination2" to 20f
        )
        val expectedEntityList = listOf(
            UpstreamDestinationEntity("destination1", 10f),
            UpstreamDestinationEntity("destination2", 20f)
        )

        val actualEntityList = upstreamDestinationsV5.toEntityList()

        assertThat(actualEntityList).isEqualTo(expectedEntityList)
    }

    @Test
    fun `GIVEN List of UpstreamDestinationsWithPercentagesV6 WHEN toEntityList THEN maps to list of UpstreamDestinationEntity correctly`() {
        val upstreamDestinationsV6List: List<UpstreamDestinationsWithPercentagesV6> = listOf(
            PiHoleUpstreamV6Data(
                ip = "1.1.1.1",
                name = "Cloudflare",
                port = 53,
                count = 10,
                statistics = PiHoleUpstreamStatisticsV6Data(0.0, 0.0)
            ) to 10f,
            PiHoleUpstreamV6Data(
                ip = "8.8.8.8",
                name = "Google",
                port = 53,
                count = 20,
                statistics = PiHoleUpstreamStatisticsV6Data(0.0, 0.0)
            ) to 20f
        )
        val expectedEntityList = listOf(
            UpstreamDestinationEntity("Cloudflare|1.1.1.1", 10f),
            UpstreamDestinationEntity("Google|8.8.8.8", 20f)
        )

        val actualEntityList = upstreamDestinationsV6List.toEntityList()

        assertThat(actualEntityList).isEqualTo(expectedEntityList)
    }

    @Test
    fun `GIVEN UpstreamDestinationsWithPercentagesV6 WHEN toEntity THEN maps to UpstreamDestinationEntity correctly`() {
        val upstreamDestinationsV6: UpstreamDestinationsWithPercentagesV6 =
            PiHoleUpstreamV6Data(
                ip = "1.1.1.1",
                name = "Cloudflare",
                port = 53,
                count = 10,
                statistics = PiHoleUpstreamStatisticsV6Data(0.0, 0.0)
            ) to 10f
        val expectedEntity = UpstreamDestinationEntity("Cloudflare|1.1.1.1", 10f)

        val actualEntity = upstreamDestinationsV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }
}
