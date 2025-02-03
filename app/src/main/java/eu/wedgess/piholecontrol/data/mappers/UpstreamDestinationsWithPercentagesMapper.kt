package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.UpstreamDestinationsWithPercentagesV5
import eu.wedgess.piholecontrol.data.model.responses.v6.UpstreamDestinationsWithPercentagesV6
import eu.wedgess.piholecontrol.domain.model.UpstreamDestinationEntity

fun UpstreamDestinationsWithPercentagesV5.toEntityList() = this.map { (destination, percentage) ->
    UpstreamDestinationEntity(
        destination = destination,
        percentage = percentage
    )
}

fun List<UpstreamDestinationsWithPercentagesV6>.toEntityList() = this.map {
    it.toEntity()
}

fun UpstreamDestinationsWithPercentagesV6.toEntity() = UpstreamDestinationEntity(
    destination = this.first.combinedName,
    percentage = this.second
)
