package eu.wedgess.piholecontrol.data.model.responses.exceptions

import eu.wedgess.piholecontrol.data.model.responses.ApiErrorResponse

class ApiErrorThrowable(val apiErrorResponse: ApiErrorResponse) : Throwable()
