package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.enums.PiHoleApiVersionData
import eu.wedgess.piholecontrol.domain.model.PiHoleApiVersionEntity

fun PiHoleApiVersionData.toEntity(): PiHoleApiVersionEntity = PiHoleApiVersionEntity[this.key]
fun PiHoleApiVersionEntity.fromEntity(): PiHoleApiVersionData = PiHoleApiVersionData[this.key]