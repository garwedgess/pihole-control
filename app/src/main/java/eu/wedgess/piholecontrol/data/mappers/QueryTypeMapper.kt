package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.v5.PiHoleQueryTypesResponseDataV5
import eu.wedgess.piholecontrol.data.model.responses.v5.QueryTypeValueDataV5
import eu.wedgess.piholecontrol.data.model.responses.v6.PiHoleQueryTypesResponseDataV6
import eu.wedgess.piholecontrol.data.model.responses.v6.QueryTypeValueDataV6
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity

fun PiHoleQueryTypesResponseDataV5.toEntityList() = this.queryTypes.asList().map { it.toEntityV5() }

fun QueryTypeValueDataV5.toEntityV5() = QueryTypeEntity(type = this.first, percentage = this.second)

fun PiHoleQueryTypesResponseDataV6.toEntityList() = this.queryTypes.asList().map { it.toEntityV6() }

fun QueryTypeValueDataV6.toEntityV6() = QueryTypeEntity(type = this.first, percentage = this.second)
