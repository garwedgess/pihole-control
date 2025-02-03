package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.enums.LogsAnswerType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@Serializable
data class PiHoleLogsResponseDataV5(
    val data: List<PiHoleLogEntryData> = listOf()
) {

    @Serializable(PiHoleLogEntryData.PiHoleLogSerializer::class)
    data class PiHoleLogEntryData(
        val timestamp: Long,
        val queryType: String,
        val requestedDomain: String,
        val client: String,
        val answerType: LogsAnswerType,
        val responseTime: Int
    ) {
        object PiHoleLogSerializer : KSerializer<PiHoleLogEntryData> {
            override val descriptor: SerialDescriptor =
                buildClassSerialDescriptor(
                    PiHoleLogEntryData::class.java.simpleName
                ) {
                    element<Int>(PiHoleLogEntryData::timestamp.name)
                    element<String>(PiHoleLogEntryData::queryType.name)
                    element<String>(PiHoleLogEntryData::requestedDomain.name)
                    element<String>(PiHoleLogEntryData::client.name)
                    element<LogsAnswerType>(PiHoleLogEntryData::answerType.name)
                }

            override fun serialize(
                encoder: Encoder,
                value: PiHoleLogEntryData
            ) {
                error("Serialization is not supported")
            }

            override fun deserialize(decoder: Decoder): PiHoleLogEntryData {
                require(decoder is JsonDecoder)

                val jsonArray = decoder.decodeJsonElement().jsonArray

                return PiHoleLogEntryData(
                    timestamp = jsonArray[0].jsonPrimitive.long,
                    queryType = jsonArray[1].jsonPrimitive.content,
                    requestedDomain = jsonArray[2].jsonPrimitive.content,
                    client = jsonArray[3].jsonPrimitive.content,
                    answerType = LogsAnswerType.entries.toTypedArray()
                        .getOrElse(jsonArray[4].jsonPrimitive.int - 1) {
                            LogsAnswerType.UNKNOWN
                        },
                    responseTime = jsonArray[7].jsonPrimitive.int
                )
            }
        }
    }
}
