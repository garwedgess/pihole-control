package eu.wedgess.mihole.ui.statistics

import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.base.UiResult
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel
import eu.wedgess.mihole.utils.UiText

interface StatisticsContract :
    UnidirectionalViewModel<StatisticsContract.UiState, StatisticsContract.Event, StatisticsContract.Effect> {

    data class UiState(
        val statistics: UiResult<PiHoleStatistics>
    ) {

        fun statistics(piHoleStatistics: PiHoleStatistics): UiState =
            this.copy(statistics = UiResult.Success(piHoleStatistics))

        fun statisticsError(errorMessage: UiText): UiState =
            this.copy(statistics = UiResult.Error(errorMessage))

        companion object {
            fun initial() = UiState(
                statistics = UiResult.Loading
            )
        }
    }

    sealed interface Effect {

    }

    sealed interface Event {
        object FetchStatistics : Event
        object ListenForConnectionChanges : Event
    }
}