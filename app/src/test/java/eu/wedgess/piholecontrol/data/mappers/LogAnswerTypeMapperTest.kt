package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import eu.wedgess.piholecontrol.domain.model.LogAnswerTypeEntity
import org.junit.Test

class LogAnswerTypeMapperTest {

    @Test
    fun `toLogAnswerTypeEntity - maps GRAVITY_BLOCK correctly`() {
        val result = LogsAnswerType.GRAVITY_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.GRAVITY_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps UPSTREAM correctly`() {
        val result = LogsAnswerType.UPSTREAM.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.UPSTREAM)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps LOCAL_CACHE correctly`() {
        val result = LogsAnswerType.LOCAL_CACHE.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.LOCAL_CACHE)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps REGEX_BLOCK correctly`() {
        val result = LogsAnswerType.REGEX_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.REGEX_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps EXACT_BLOCK correctly`() {
        val result = LogsAnswerType.EXACT_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.EXACT_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps EXTERNAL_IP_BLOCK correctly`() {
        val result = LogsAnswerType.EXTERNAL_IP_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.EXTERNAL_IP_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps EXTERNAL_NULL_BLOCK correctly`() {
        val result = LogsAnswerType.EXTERNAL_NULL_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.EXTERNAL_NULL_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps EXTERNAL_NXRA_BLOCK correctly`() {
        val result = LogsAnswerType.EXTERNAL_NXRA_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.EXTERNAL_NXRA_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps CNAME_GRAVITY_BLOCK correctly`() {
        val result = LogsAnswerType.CNAME_GRAVITY_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.CNAME_GRAVITY_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps CNAME_REGEX_BLOCK correctly`() {
        val result = LogsAnswerType.CNAME_REGEX_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.CNAME_REGEX_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps CNAME_EXACT_BLOCK correctly`() {
        val result = LogsAnswerType.CNAME_EXACT_BLOCK.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.CNAME_EXACT_BLOCK)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps RETRIED correctly`() {
        val result = LogsAnswerType.RETRIED.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.RETRIED)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps RETRIED_IGNORED correctly`() {
        val result = LogsAnswerType.RETRIED_IGNORED.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.RETRIED_IGNORED)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps ALREADY_FORWARDED correctly`() {
        val result = LogsAnswerType.ALREADY_FORWARDED.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.ALREADY_FORWARDED)
    }

    @Test
    fun `toLogAnswerTypeEntity - maps UNKNOWN correctly`() {
        val result = LogsAnswerType.UNKNOWN.toLogAnswerTypeEntity()
        assertThat(result).isEqualTo(LogAnswerTypeEntity.UNKNOWN)
    }
}
