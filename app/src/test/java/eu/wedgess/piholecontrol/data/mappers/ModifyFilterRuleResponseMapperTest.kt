package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleAddFilterRuleResponseDataV6
import org.junit.Test

class ModifyFilterRuleResponseMapperTest {

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse to ModifyFilterRuleResponseEntity correctly`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = true,
            message = "Rule modified successfully"
        )

        val responseEntity = piHoleResponse.toEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isEqualTo("Rule modified successfully")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with failure`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = false,
            message = "Failed to modify rule"
        )

        val responseEntity = piHoleResponse.toEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isEqualTo("Failed to modify rule")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with null message`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = true,
            message = null
        )

        val responseEntity = piHoleResponse.toEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isNull()
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with default values`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5()

        val responseEntity = piHoleResponse.toEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isNull()
    }

    @Test
    fun `GIVEN successful response WHEN mapping to entity THEN returns success and joined success messages`() {
        val response = PiHoleAddFilterRuleResponseDataV6(
            domains = emptyList(),
            processed = PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData(
                success = listOf(
                    PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedItemResponseData(
                        "Success 1"
                    ),
                    PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedItemResponseData(
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
        val response = PiHoleAddFilterRuleResponseDataV6(
            domains = emptyList(),
            processed = PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData(
                success = emptyList(),
                errors = listOf(
                    PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedErrorResponseData(
                        "Error item 1",
                        "Error 1"
                    ),
                    PiHoleAddFilterRuleResponseDataV6.PiHoleAddFilterRuleProcessedResponseData.PiHoleAddFilterRuleProcessedErrorResponseData(
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
