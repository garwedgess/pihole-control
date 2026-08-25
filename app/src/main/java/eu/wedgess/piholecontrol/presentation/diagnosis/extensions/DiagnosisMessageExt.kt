package eu.wedgess.piholecontrol.presentation.diagnosis.extensions

import eu.wedgess.piholecontrol.domain.model.DiagnosisMessageEntity
import eu.wedgess.piholecontrol.presentation.diagnosis.model.DiagnosisMessageInfo
import eu.wedgess.piholecontrol.presentation.diagnosis.model.toDiagnosisSeverity

fun DiagnosisMessageEntity.toInfo() = DiagnosisMessageInfo(
    id = id,
    timestamp = timestamp,
    type = type,
    severity = type.toDiagnosisSeverity(),
    plain = plain,
    url = url
)
