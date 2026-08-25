package eu.wedgess.piholecontrol.presentation.diagnosis.model

data class DiagnosisMessageInfo(
    val id: Int,
    val timestamp: Long,
    val type: String,
    val severity: DiagnosisSeverity,
    val plain: String,
    val url: String?
)
