package eu.wedgess.piholecontrol.domain.model

data class DiagnosisMessageEntity(
    val id: Int,
    val timestamp: Long,
    val type: String,
    val plain: String,
    val url: String?
)
