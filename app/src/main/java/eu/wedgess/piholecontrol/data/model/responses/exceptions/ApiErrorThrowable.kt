package eu.wedgess.piholecontrol.data.model.responses.exceptions

import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse

class ApiErrorThrowable(val apiErrorResponse: ApiErrorResponse) : Throwable() {
    override val message: String = when (apiErrorResponse) {
        is ApiErrorResponse.V5 -> apiErrorResponse.errorMessage
        is ApiErrorResponse.V6 -> apiErrorResponse.serverError.error?.message
            ?: apiErrorResponse.serverError.session?.message ?: "Unknown"
    }
}
