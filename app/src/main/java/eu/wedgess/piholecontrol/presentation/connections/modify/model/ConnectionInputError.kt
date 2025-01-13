package eu.wedgess.piholecontrol.presentation.connections.modify.model

import eu.wedgess.piholecontrol.utils.UiText

sealed class ConnectionInputError(open val error: UiText) {
    data class Name(override val error: UiText) : ConnectionInputError(error)
    data class Host(override val error: UiText) : ConnectionInputError(error)
    data class Port(override val error: UiText) : ConnectionInputError(error)
    data class Password(override val error: UiText) : ConnectionInputError(error)
    data class ApiKey(override val error: UiText) : ConnectionInputError(error)
}
