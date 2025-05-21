package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.PiHoleFilterRuleType
import eu.wedgess.piholecontrol.domain.model.FilterRuleTypeEntity
import org.junit.Test

class FilterRuleTypeMapperTest {

    @Test
    fun `toFilterTypeRuleEntity - maps ALLOW correctly`() {
        val result = PiHoleFilterRuleType.ALLOW.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.ALLOW)
    }

    @Test
    fun `toFilterTypeRuleEntity - maps BLOCK correctly`() {
        val result = PiHoleFilterRuleType.DENY.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.DENY)
    }

    @Test
    fun `toFilterTypeRuleEntity - maps REGEX_ALLOW correctly`() {
        val result = PiHoleFilterRuleType.REGEX_ALLOW.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.REGEX_ALLOW)
    }

    @Test
    fun `toFilterTypeRuleEntity - maps REGEX_BLOCK correctly`() {
        val result = PiHoleFilterRuleType.REGEX_DENY.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.REGEX_DENY)
    }
}
