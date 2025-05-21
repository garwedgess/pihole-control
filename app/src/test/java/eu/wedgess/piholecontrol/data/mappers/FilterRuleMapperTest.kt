package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleFilterRulesResponseData
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test

class FilterRuleMapperTest {

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `GIVEN filter rule data WHEN mapping to entity THEN dates are formatted correctly`() {
        val data = PiHoleFilterRulesResponseData.PiHoleFilterRuleData(
            domain = "example.com",
            unicode = "",
            type = "allow",
            kind = "regex",
            comment = "Test comment",
            groups = listOf(1, 2),
            enabled = true,
            id = 123,
            dateAdded = 1678886400, // Example timestamp (2023-03-15T00:00:00Z)
            dateModified = 1678886400
        )
        val entity = data.toFilterRuleEntity()

        assertThat(entity.id).isEqualTo(data.id)
        assertThat(entity.domain).isEqualTo(data.domain)
        assertThat(entity.comment).isEqualTo(data.comment)
        assertThat(entity.groups).isEqualTo(data.groups)
        assertThat(entity.enabled).isEqualTo(data.enabled)
        assertThat(entity.type).isEqualTo(FilterRuleTypeEntity.REGEX_ALLOW)
    }

    @Test
    fun `GIVEN filter rule data WHEN mapping to entity THEN type is converted correctly`() {
        val data = PiHoleFilterRulesResponseData.PiHoleFilterRuleData(
            domain = "example.com",
            unicode = "",
            type = "deny",
            kind = "exact",
            comment = "Test comment",
            groups = listOf(1, 2),
            enabled = true,
            id = 123,
            dateAdded = 1678886400,
            dateModified = 1678886400
        )

        val entity = data.toFilterRuleEntity()

        assertThat(entity.id).isEqualTo(data.id)
        assertThat(entity.domain).isEqualTo(data.domain)
        assertThat(entity.comment).isEqualTo(data.comment)
        assertThat(entity.groups).isEqualTo(data.groups)
        assertThat(entity.enabled).isEqualTo(data.enabled)
        assertThat(entity.type).isEqualTo(FilterRuleTypeEntity.DENY)
    }
}
