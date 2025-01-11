package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleSummaryResponseDataV5
import org.junit.Test

class SummaryMapperTest {

    @Test
    fun `toSummaryEntity - maps PiHoleSummary to SummaryEntity correctly`() {
        val piHoleSummaryResponseDataV5 = PiHoleSummaryResponseDataV5(
            domainsBeingBlocked = 100000,
            dnsQueriesToday = 5000,
            adsBlockedToday = 1000,
            adsPercentageToday = 20.0f,
            uniqueClients = 50
        )

        val summaryEntity = piHoleSummaryResponseDataV5.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(100000)
        assertThat(summaryEntity.dnsQueries).isEqualTo(5000)
        assertThat(summaryEntity.adsBlocked).isEqualTo(1000)
        assertThat(summaryEntity.adsPercentage).isEqualTo(20.0f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(50)
    }

    @Test
    fun `toSummaryEntity - maps PiHoleSummary with default values`() {
        val piHoleSummaryResponseDataV5 = PiHoleSummaryResponseDataV5()

        val summaryEntity = piHoleSummaryResponseDataV5.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(0)
        assertThat(summaryEntity.dnsQueries).isEqualTo(0)
        assertThat(summaryEntity.adsBlocked).isEqualTo(0)
        assertThat(summaryEntity.adsPercentage).isEqualTo(0f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(0)
    }

    @Test
    fun `toSummaryEntity - maps PiHoleSummary with different values`() {
        val piHoleSummaryResponseDataV5 = PiHoleSummaryResponseDataV5(
            domainsBeingBlocked = 200000,
            dnsQueriesToday = 10000,
            adsBlockedToday = 2000,
            adsPercentageToday = 15.5f,
            uniqueClients = 100
        )

        val summaryEntity = piHoleSummaryResponseDataV5.toSummaryEntity()

        assertThat(summaryEntity.domainsBlocked).isEqualTo(200000)
        assertThat(summaryEntity.dnsQueries).isEqualTo(10000)
        assertThat(summaryEntity.adsBlocked).isEqualTo(2000)
        assertThat(summaryEntity.adsPercentage).isEqualTo(15.5f)
        assertThat(summaryEntity.uniqueClients).isEqualTo(100)
    }
}
