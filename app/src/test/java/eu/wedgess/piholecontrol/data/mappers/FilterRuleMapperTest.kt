package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleFilterRules
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import eu.wedgess.piholecontrol.initThreeTen
import org.junit.Before
import org.junit.Test
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class FilterRuleMapperTest {

    @Before
    fun setup() {
        initThreeTen()
    }

    @Test
    fun `toFilterRuleEntity - maps PiHoleFilterRule to FilterRuleEntity correctly`() {
        val piHoleFilterRule = PiHoleFilterRules.PiHoleFilterRule(
            id = 1,
            enabled = 1,
            comment = "Test Rule",
            dateAdded = 1678886400, // March 15, 2023 00:00:00 UTC
            dateModified = 1678972800, // March 16, 2023 00:00:00 UTC
            domain = "example.com",
            groups = listOf(1, 2),
            type = PiHoleFilterRuleType.REGEX_ALLOW
        )

        val filterRuleEntity = piHoleFilterRule.toFilterRuleEntity()

        assertThat(filterRuleEntity.id).isEqualTo(1)
        assertThat(filterRuleEntity.enabled).isTrue()
        assertThat(filterRuleEntity.comment).isEqualTo("Test Rule")
        assertThat(filterRuleEntity.dateAdded).isEqualTo(
            Instant.ofEpochSecond(1678886400)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .replace("T", " ")
        )
        assertThat(filterRuleEntity.dateModified).isEqualTo(
            Instant.ofEpochSecond(1678972800)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .replace("T", " ")
        )
        assertThat(filterRuleEntity.domain).isEqualTo("example.com")
        assertThat(filterRuleEntity.groups).isEqualTo(listOf(1, 2))
        assertThat(filterRuleEntity.type).isEqualTo(FilterRuleTypeEntity.REGEX_ALLOW)
    }

    @Test
    fun `toFilterRuleEntity - maps PiHoleFilterRule with disabled rule`() {
        val piHoleFilterRule = PiHoleFilterRules.PiHoleFilterRule(
            id = 2,
            enabled = 0,
            comment = "Disabled Rule",
            dateAdded = 1678886400,
            dateModified = 1678972800,
            domain = "disabled.com",
            groups = emptyList(),
            type = PiHoleFilterRuleType.REGEX_BLOCK
        )

        val filterRuleEntity = piHoleFilterRule.toFilterRuleEntity()

        assertThat(filterRuleEntity.enabled).isFalse()
        assertThat(filterRuleEntity.groups).isEmpty()
        assertThat(filterRuleEntity.type).isEqualTo(FilterRuleTypeEntity.REGEX_BLOCK)
    }

    @Test
    fun `toFilterRulesEntity - maps PiHoleFilterRules to List of FilterRuleEntity`() {
        val piHoleFilterRules = PiHoleFilterRules(
            rulesList = listOf(
                PiHoleFilterRules.PiHoleFilterRule(
                    id = 1,
                    enabled = 1,
                    comment = "Rule 1",
                    dateAdded = 1678886400,
                    dateModified = 1678972800,
                    domain = "example1.com",
                    groups = listOf(1),
                    type = PiHoleFilterRuleType.ALLOW
                ),
                PiHoleFilterRules.PiHoleFilterRule(
                    id = 2,
                    enabled = 0,
                    comment = "Rule 2",
                    dateAdded = 1678886400,
                    dateModified = 1678972800,
                    domain = "example2.com",
                    groups = listOf(2),
                    type = PiHoleFilterRuleType.BLOCK
                )
            )
        )

        val filterRuleEntities = piHoleFilterRules.toFilterRulesEntity()

        assertThat(filterRuleEntities).hasSize(2)
        assertThat(filterRuleEntities[0].id).isEqualTo(1)
        assertThat(filterRuleEntities[0].enabled).isTrue()
        assertThat(filterRuleEntities[0].comment).isEqualTo("Rule 1")
        assertThat(filterRuleEntities[0].domain).isEqualTo("example1.com")
        assertThat(filterRuleEntities[0].groups).isEqualTo(listOf(1))
        assertThat(filterRuleEntities[0].type).isEqualTo(FilterRuleTypeEntity.ALLOW)
        assertThat(filterRuleEntities[1].id).isEqualTo(2)
        assertThat(filterRuleEntities[1].enabled).isFalse()
        assertThat(filterRuleEntities[1].comment).isEqualTo("Rule 2")
        assertThat(filterRuleEntities[1].domain).isEqualTo("example2.com")
        assertThat(filterRuleEntities[1].groups).isEqualTo(listOf(2))
        assertThat(filterRuleEntities[1].type).isEqualTo(FilterRuleTypeEntity.BLOCK)
    }

    @Test
    fun `toFilterRulesEntity - maps empty PiHoleFilterRules to empty List`() {
        val piHoleFilterRules = PiHoleFilterRules(rulesList = emptyList())

        val filterRuleEntities = piHoleFilterRules.toFilterRulesEntity()

        assertThat(filterRuleEntities).isEmpty()
    }
}
