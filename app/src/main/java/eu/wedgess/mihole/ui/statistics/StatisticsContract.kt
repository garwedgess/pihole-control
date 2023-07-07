package eu.wedgess.mihole.ui.statistics

import eu.wedgess.mihole.data.model.PiHoleStatistics
import eu.wedgess.mihole.ui.base.Resource
import eu.wedgess.mihole.ui.base.UnidirectionalViewModel

interface StatisticsContract :
    UnidirectionalViewModel<StatisticsContract.UiState, StatisticsContract.Event, StatisticsContract.Effect> {

    data class UiState(
        val statistics: Resource<PiHoleStatistics>
    ) {

        fun statistics(piHoleStatistics: PiHoleStatistics): UiState =
            this.copy(statistics = Resource.Success(piHoleStatistics))

        fun statisticsError(errorMessage: String): UiState =
            this.copy(statistics = Resource.Error(errorMessage))

        companion object {
            fun initial() = UiState(
                statistics = Resource.Loading
            )
        }
    }

    sealed interface Effect {

    }

    sealed interface Event {
        object FetchStatistics : Event
    }
}