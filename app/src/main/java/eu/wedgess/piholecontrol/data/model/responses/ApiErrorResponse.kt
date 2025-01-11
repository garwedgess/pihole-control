package eu.wedgess.piholecontrol.data.model.responses

import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleErrorResponseDataV6

sealed class ApiErrorResponse {
    data class V5(val errorMessage: String) : ApiErrorResponse()
    data class V6(val serverError: PiHoleErrorResponseDataV6) : ApiErrorResponse()
}
