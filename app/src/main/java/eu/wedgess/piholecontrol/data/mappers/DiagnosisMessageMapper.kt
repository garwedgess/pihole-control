package eu.wedgess.piholecontrol.data.mappers

import eu.wedgess.piholecontrol.data.model.responses.PiHoleDiagnosisMessagesResponseData
import eu.wedgess.piholecontrol.domain.model.DiagnosisMessageEntity

fun PiHoleDiagnosisMessagesResponseData.MessageData.toEntity() = DiagnosisMessageEntity(
    id = id,
    timestamp = timestamp.toLong(),
    type = type,
    plain = plain,
    url = url
)
