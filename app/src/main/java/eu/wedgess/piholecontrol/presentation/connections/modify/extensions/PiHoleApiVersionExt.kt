package eu.wedgess.piholecontrol.presentation.connections.modify.extensions

import eu.wedgess.piholecontrol.domain.model.PiHoleApiVersionEntity
import eu.wedgess.piholecontrol.presentation.connections.modify.model.PiHoleApiVersion

fun PiHoleApiVersion.toEntity() = PiHoleApiVersionEntity[this.key]
fun PiHoleApiVersionEntity.fromEntity() = PiHoleApiVersion[this.key]