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
        val result = PiHoleFilterRuleType.BLOCK.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.BLOCK)
    }

    @Test
    fun `toFilterTypeRuleEntity - maps REGEX_ALLOW correctly`() {
        val result = PiHoleFilterRuleType.REGEX_ALLOW.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.REGEX_ALLOW)
    }

    @Test
    fun `toFilterTypeRuleEntity - maps REGEX_BLOCK correctly`() {
        val result = PiHoleFilterRuleType.REGEX_BLOCK.toFilterTypeRuleEntity()
        assertThat(result).isEqualTo(FilterRuleTypeEntity.REGEX_BLOCK)
    }

    @Test
    fun `toPiHoleFilterRuleType - maps ALLOW correctly`() {
        val result = FilterRuleTypeEntity.ALLOW.toPiHoleFilterRuleType()
        assertThat(result).isEqualTo(PiHoleFilterRuleType.ALLOW)
    }

    @Test
    fun `toPiHoleFilterRuleType - maps BLOCK correctly`() {
        val result = FilterRuleTypeEntity.BLOCK.toPiHoleFilterRuleType()
        assertThat(result).isEqualTo(PiHoleFilterRuleType.BLOCK)
    }

    @Test
    fun `toPiHoleFilterRuleType - maps REGEX_ALLOW correctly`() {
        val result = FilterRuleTypeEntity.REGEX_ALLOW.toPiHoleFilterRuleType()
        assertThat(result).isEqualTo(PiHoleFilterRuleType.REGEX_ALLOW)
    }

    @Test
    fun `toPiHoleFilterRuleType - maps REGEX_BLOCK correctly`() {
        val result = FilterRuleTypeEntity.REGEX_BLOCK.toPiHoleFilterRuleType()
        assertThat(result).isEqualTo(PiHoleFilterRuleType.REGEX_BLOCK)
    }
}
