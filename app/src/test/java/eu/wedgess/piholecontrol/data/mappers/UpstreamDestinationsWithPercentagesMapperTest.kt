package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleUpstreamStatisticsData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleUpstreamData
import eu.wedgess.piholecontrol.data.model.responses.UpstreamDestinationsWithPercentages
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity
import org.junit.Test

class UpstreamDestinationsWithPercentagesMapperTest {

    @Test
    fun `GIVEN List of UpstreamDestinationsWithPercentagesV6 WHEN toEntityList THEN maps to list of UpstreamDestinationEntity correctly`() {
        val upstreamDestinationsV6List: List<UpstreamDestinationsWithPercentages> = listOf(
            PiHoleUpstreamData(
                ip = "1.1.1.1",
                name = "Cloudflare",
                port = 53,
                count = 10,
                statistics = PiHoleUpstreamStatisticsData(0.0, 0.0)
            ) to 10f,
            PiHoleUpstreamData(
                ip = "8.8.8.8",
                name = "Google",
                port = 53,
                count = 20,
                statistics = PiHoleUpstreamStatisticsData(0.0, 0.0)
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
        val upstreamDestinationsV6: UpstreamDestinationsWithPercentages =
            PiHoleUpstreamData(
                ip = "1.1.1.1",
                name = "Cloudflare",
                port = 53,
                count = 10,
                statistics = PiHoleUpstreamStatisticsData(0.0, 0.0)
            ) to 10f
        val expectedEntity = UpstreamDestinationEntity("Cloudflare|1.1.1.1", 10f)

        val actualEntity = upstreamDestinationsV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }
}
