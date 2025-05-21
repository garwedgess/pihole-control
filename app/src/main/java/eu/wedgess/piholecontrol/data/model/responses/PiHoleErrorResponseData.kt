package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PiHoleErrorResponseData(
    @SerialName("error")
    val error: ErrorData? = null,
    @SerialName("session")
    val session: PiHoleAuthSessionStatusResponseData.PiHoleAuthSession? = null,
    @SerialName("took")
    val took: Double
) {
    @Serializable
    data class ErrorData(
        @SerialName("key") val key: String,
        @SerialName("message") val message: String,
        @SerialName("hint") val hint: String? = null
    )

    companion object {
        val unauthorized = PiHoleErrorResponseData(
            error = ErrorData(
                key = "unauthorized",
                message = "Unauthorized",
                hint = null
            ),
            took = 1.323223114013672E-4
        )
        val notFound = PiHoleErrorResponseData(
            error = ErrorData(
                key = "not_found",
                message = "Not found",
                hint = null
            ),
            took = 0.00021076202392578125
        )
    }
}
