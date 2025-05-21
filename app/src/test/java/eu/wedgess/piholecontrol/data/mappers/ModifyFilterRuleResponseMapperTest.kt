package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleAddFilterRuleResponseData
import org.junit.Test

class ModifyFilterRuleResponseMapperTest {

    @Test
    fun `GIVEN successful response WHEN mapping to entity THEN returns success and joined success messages`() {
        val response = PiHoleAddFilterRuleResponseData(
            domains = emptyList(),
            processed = PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData(
                success = listOf(
                    PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedItemResponseData(
                        "Success 1"
                    ),
                    PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedItemResponseData(
                        "Success 2"
                    )
                ),
                errors = emptyList()
            ),
            took = 0.0
        )
        val entity = response.toEntity()

        assertThat(entity.success).isTrue()
        assertThat(entity.message).isEqualTo("Success 1\nSuccess 2")
    }

    @Test
    fun `GIVEN unsuccessful response WHEN mapping to entity THEN returns failure and joined error messages`() {
        val response = PiHoleAddFilterRuleResponseData(
            domains = emptyList(),
            processed = PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData(
                success = emptyList(),
                errors = listOf(
                    PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedErrorResponseData(
                        "Error item 1",
                        "Error 1"
                    ),
                    PiHoleAddFilterRuleResponseData.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedErrorResponseData(
                        "Error item 2",
                        "Error 2"
                    )
                )
            ),
            took = 0.0
        )
        val entity = response.toEntity()

        assertThat(entity.success).isFalse()
        assertThat(entity.message).isEqualTo("Error item 1: Error 1\nError item 2: Error 2")
    }
}
