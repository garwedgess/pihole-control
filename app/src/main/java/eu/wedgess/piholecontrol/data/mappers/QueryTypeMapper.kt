package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleQueryTypesResponseData
import eu.wedgess.piholecontrol.data.model.responses.QueryTypeValueData
import eu.wedgess.piholecontrol.domain.model.QueryTypeEntity

fun PiHoleQueryTypesResponseData.toEntityList() = this.queryTypes.asList().map { it.toEntityV6() }

fun QueryTypeValueData.toEntityV6() = QueryTypeEntity(type = this.first, percentage = this.second)
