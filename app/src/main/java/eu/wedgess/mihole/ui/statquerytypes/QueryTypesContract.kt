package eu.wedgess.mihole.ui.statquerytypes

import eu.wedgess.mihole.data.model.responses.QueryTypes
import eu.wedgess.mihole.utils.UiText

interface QueryTypesContract {

    data class UiState(
        val queryTypes: QueryTypes
    )
}