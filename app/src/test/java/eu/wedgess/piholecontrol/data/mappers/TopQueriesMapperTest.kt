package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.TopQueryData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueriesCombinedResponseData
import eu.wedgess.piholecontrol.data.model.responses.PiHoleTopQueryData
import eu.wedgess.piholecontrol.domain.model.TopDomainEntity
import eu.wedgess.piholecontrol.domain.model.TopQueriesEntity
import org.junit.Test

class TopQueriesMapperTest {

    @Test
    fun `GIVEN PiHoleTopQueriesCombinedResponseV6Data WHEN toEntity THEN maps to TopQueriesEntity correctly AND applies sorting`() {
        val responseV6 = PiHoleTopQueriesCombinedResponseData(
            permitted = listOf(
                PiHoleTopQueryData("domain1", 10),
                PiHoleTopQueryData("domain2", 5)
            ),
            blocked = listOf(
                PiHoleTopQueryData("domain3", 2),
                PiHoleTopQueryData("domain4", 3)
            )
        )
        val expectedEntity = TopQueriesEntity(
            allowed = listOf(
                TopDomainEntity("domain1", 10, 66.66f),
                TopDomainEntity("domain2", 5, 33.33f)
            ),
            blocked = listOf(
                TopDomainEntity("domain4", 3, 60f),
                TopDomainEntity("domain3", 2, 40f)
            )
        )

        val actualEntity = responseV6.toEntity()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN TopQueryData WHEN toEntity THEN maps to TopDomainEntity correctly`() {
        val topQueryData = TopQueryData(
            domain = "example.com",
            hits = 10,
            percentage = 25.5f
        )
        val expectedTopDomainEntity = TopDomainEntity(
            domain = "example.com",
            hits = 10,
            percentage = 25.5f
        )

        val actualTopDomainEntity = topQueryData.toEntity()

        assertThat(actualTopDomainEntity).isEqualTo(expectedTopDomainEntity)
    }
}
