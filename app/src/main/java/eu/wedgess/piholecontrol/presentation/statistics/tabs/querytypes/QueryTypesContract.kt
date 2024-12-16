package eu.wedgess.piholecontrol.presentation.statistics.tabs.querytypes

import eu.wedgess.piholecontrol.data.model.responses.QueryTypes

interface QueryTypesContract {

    data class UiState(
        val queryTypes: QueryTypes
    )
}
