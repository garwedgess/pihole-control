package eu.wedgess.mihole.data.utils.serializers

import eu.wedgess.mihole.data.model.PiHoleLog
import eu.wedgess.mihole.data.model.enums.LogsAnswerType
import kotlinx.serialization.KSerializer
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

object PiHoleLogSerializer : KSerializer<PiHoleLog> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(PiHoleLog::class.simpleName!!) {
            element<Int>(PiHoleLog::timestamp.name)
            element<String>(PiHoleLog::queryType.name)
            element<String>(PiHoleLog::requestedDomain.name)
            element<String>(PiHoleLog::client.name)
            element<LogsAnswerType>(PiHoleLog::answerType.name)
        }

    override fun serialize(encoder: Encoder, value: PiHoleLog) {
        error("Serialization is not supported")
    }

    override fun deserialize(decoder: Decoder): PiHoleLog {
        require(decoder is JsonDecoder)

        val jsonArray = decoder.decodeJsonElement().jsonArray

        return PiHoleLog(
            timestamp = jsonArray[0].jsonPrimitive.long,
            queryType = jsonArray[1].jsonPrimitive.content,
            requestedDomain = jsonArray[2].jsonPrimitive.content,
            client = jsonArray[3].jsonPrimitive.content,
            answerType = LogsAnswerType.values()
                .getOrElse(jsonArray[4].jsonPrimitive.int - 1) { LogsAnswerType.UNKNOWN },
            responseTime = jsonArray[7].jsonPrimitive.int
        )
    }
}