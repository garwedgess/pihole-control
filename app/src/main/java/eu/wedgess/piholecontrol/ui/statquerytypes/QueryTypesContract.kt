package eu.wedgess.piholecontrol.ui.statquerytypes

import eu.wedgess.piholecontrol.data.model.responses.QueryTypes

interface QueryTypesContract {

    data class UiState(
        val queryTypes: QueryTypes
    )
}