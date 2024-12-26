package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.PiHoleModifyFilterRuleResponse
import org.junit.Test

class ModifyFilterRuleResponseMapperTest {

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse to ModifyFilterRuleResponseEntity correctly`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponse(
            success = true,
            message = "Rule modified successfully"
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isEqualTo("Rule modified successfully")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with failure`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponse(
            success = false,
            message = "Failed to modify rule"
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isEqualTo("Failed to modify rule")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with null message`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponse(
            success = true,
            message = null
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isNull()
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with default values`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponse()

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isNull()
    }
}
