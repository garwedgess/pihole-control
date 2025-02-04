package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.QueryTypeValueDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.QueryTypeValueDataV6
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity
import org.junit.Test

class QueryTypeMapperTest {

    @Test
    fun `GIVEN PiHoleQueryTypesResponseDataV5 WHEN toEntityList THEN maps to list of QueryTypeEntity correctly`() {
        val responseV5 = PiHoleQueryTypesResponseDataV5(
            queryTypes = PiHoleQueryTypesResponseDataV5.QueryTypeData(
                aipV4 = 10f,
                aaaaIpV6 = 20f,
                any = 0f,
                srv = 30f,
                soa = 0f,
                ptr = 40f,
                txt = 0f,
                naptr = 50f,
                mx = 0f,
                ds = 60f,
                rrsig = 0f,
                dnsKey = 70f,
                ns = 0f,
                other = 80f,
                svcb = 0f,
                http = 90f
            )
        )
        val expectedEntityList = listOf(
            QueryTypeEntity("A (IPv4)", 10f),
            QueryTypeEntity("AAAA (IPv6)", 20f),
            QueryTypeEntity("SRV", 30f),
            QueryTypeEntity("PTR", 40f),
            QueryTypeEntity("NAPTR", 50f),
            QueryTypeEntity("DS", 60f),
            QueryTypeEntity("DNSKey", 70f),
            QueryTypeEntity("OTHER", 80f),
            QueryTypeEntity("HTTPS", 90f)
        )

        val actualEntityList = responseV5.toEntityList()

        assertThat(actualEntityList).isEqualTo(expectedEntityList)
    }

    @Test
    fun `GIVEN QueryTypeValueDataV5 WHEN toEntityV5 THEN maps to QueryTypeEntity correctly`() {
        val queryTypeValueV5: QueryTypeValueDataV5 = "A (IPv4)" to 10f
        val expectedEntity = QueryTypeEntity("A (IPv4)", 10f)

        val actualEntity = queryTypeValueV5.toEntityV5()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN PiHoleQueryTypesResponseDataV6 WHEN toEntityList THEN maps to list of QueryTypeEntity correctly`() {
        val responseV6 = PiHoleQueryTypesResponseDataV6(
            queryTypes = PiHoleQueryTypesResponseDataV6.QueryTypes(
                a = 10,
                aaaa = 20,
                any = 0,
                srv = 30,
                soa = 0,
                ptr = 40,
                txt = 0,
                naptr = 50,
                mx = 0,
                ds = 60,
                rrsig = 0,
                dnskey = 70,
                ns = 0,
                other = 80,
                svcb = 0,
                https = 90
            ),
            took = 0.0
        )
        val expectedEntityList = listOf(
            QueryTypeEntity("A (IPv4)", 2.22f),
            QueryTypeEntity("AAAA (IPv6)", 4.44f),
            QueryTypeEntity("SRV", 6.66f),
            QueryTypeEntity("PTR", 8.88f),
            QueryTypeEntity("NAPTR", 11.11f),
            QueryTypeEntity("DS", 13.33f),
            QueryTypeEntity("DNSKey", 15.55f),
            QueryTypeEntity("OTHER", 17.77f),
            QueryTypeEntity("HTTPS", 20.0f)
        )

        val actualEntityList = responseV6.toEntityList()

        assertThat(actualEntityList).isEqualTo(expectedEntityList)
    }

    @Test
    fun `GIVEN QueryTypeValueDataV6 WHEN toEntityV6 THEN maps to QueryTypeEntity correctly`() {
        val queryTypeValueV6: QueryTypeValueDataV6 = "A (IPv4)" to 10f
        val expectedEntity = QueryTypeEntity("A (IPv4)", 10f)

        val actualEntity = queryTypeValueV6.toEntityV6()

        assertThat(actualEntity).isEqualTo(expectedEntity)
    }

    @Test
    fun `GIVEN PiHoleQueryTypesResponseDataV5 with empty data WHEN toEntityList THEN returns empty list`() {
        val responseV5 = PiHoleQueryTypesResponseDataV5(
            queryTypes = PiHoleQueryTypesResponseDataV5.QueryTypeData()
        )

        val actualEntityList = responseV5.toEntityList()

        assertThat(actualEntityList).isEmpty()
    }

    @Test
    fun `GIVEN PiHoleQueryTypesResponseDataV6 with empty data WHEN toEntityList THEN returns empty list`() {
        val responseV6 = PiHoleQueryTypesResponseDataV6(
            queryTypes = PiHoleQueryTypesResponseDataV6.QueryTypes(
                a = 0,
                aaaa = 0,
                any = 0,
                srv = 0,
                soa = 0,
                ptr = 0,
                txt = 0,
                naptr = 0,
                mx = 0,
                ds = 0,
                rrsig = 0,
                dnskey = 0,
                ns = 0,
                other = 0,
                svcb = 0,
                https = 0
            ),
            took = 0.0
        )

        val actualEntityList = responseV6.toEntityList()

        assertThat(actualEntityList).isEmpty()
    }
}
