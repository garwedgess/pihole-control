package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleModifyFilterRuleResponseDataV5
import org.junit.Test

class ModifyFilterRuleResponseMapperTest {

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse to ModifyFilterRuleResponseEntity correctly`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = true,
            message = "Rule modified successfully"
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isEqualTo("Rule modified successfully")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with failure`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = false,
            message = "Failed to modify rule"
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isEqualTo("Failed to modify rule")
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with null message`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5(
            success = true,
            message = null
        )

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isTrue()
        assertThat(responseEntity.message).isNull()
    }

    @Test
    fun `toModifyFilterRuleResponseEntity - maps PiHoleModifyFilterRuleResponse with default values`() {
        val piHoleResponse = PiHoleModifyFilterRuleResponseDataV5()

        val responseEntity = piHoleResponse.toModifyFilterRuleResponseEntity()

        assertThat(responseEntity.success).isFalse()
        assertThat(responseEntity.message).isNull()
    }
}
