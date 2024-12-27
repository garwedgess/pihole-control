package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleSummary
import org.junit.Test

class SummaryMapperTest {

    @Test
    fun `toSummaryEntity - maps PiHoleSummary to SummaryEntity correctly`() {
        val piHoleSummary = PiHoleSummary(
            domainsBeingBlocked = 100000,
            dnsQueriesToday = 5000,
            adsBlockedToday = 1000,
            adsPercentageToday = 20.0f,
            uniqueClients = 50
        )

        val summaryEntity = piHoleSummary.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(100000)
        assertThat(summaryEntity.dnsQueries).isEqualTo(5000)
        assertThat(summaryEntity.adsBlocked).isEqualTo(1000)
        assertThat(summaryEntity.adsPercentage).isEqualTo(20.0f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(50)
    }

    @Test
    fun `toSummaryEntity - maps PiHoleSummary with default values`() {
        val piHoleSummary = PiHoleSummary()

        val summaryEntity = piHoleSummary.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(0)
        assertThat(summaryEntity.dnsQueries).isEqualTo(0)
        assertThat(summaryEntity.adsBlocked).isEqualTo(0)
        assertThat(summaryEntity.adsPercentage).isEqualTo(0f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(0)
    }

    @Test
    fun `toSummaryEntity - maps PiHoleSummary with different values`() {
        val piHoleSummary = PiHoleSummary(
            domainsBeingBlocked = 200000,
            dnsQueriesToday = 10000,
            adsBlockedToday = 2000,
            adsPercentageToday = 15.5f,
            uniqueClients = 100
        )

        val summaryEntity = piHoleSummary.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(200000)
        assertThat(summaryEntity.dnsQueries).isEqualTo(10000)
        assertThat(summaryEntity.adsBlocked).isEqualTo(2000)
        assertThat(summaryEntity.adsPercentage).isEqualTo(15.5f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(100)
    }
}
