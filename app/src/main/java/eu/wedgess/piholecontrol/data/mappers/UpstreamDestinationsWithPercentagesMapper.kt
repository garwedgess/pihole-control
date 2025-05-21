package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.UpstreamDestinationsWithPercentages
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity

fun List<UpstreamDestinationsWithPercentages>.toEntityList() = this.map {
    it.toEntity()
}

fun UpstreamDestinationsWithPercentages.toEntity() = UpstreamDestinationEntity(
    destination = this.first.combinedName,
    percentage = this.second
)
