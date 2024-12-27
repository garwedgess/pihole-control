package eu.wedgess.piholecontrol.data.model.responses

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class PiHoleTopQueries(
    @Serializable(with = MapOrObjectSerializer::class)
    @SerialName("top_ads")
    val topAds: Map<String, Int> = mapOf(),
    @Serializable(with = MapOrObjectSerializer::class)
    @SerialName("top_queries")
    val topQueries: Map<String, Int> = mapOf()
)

private object MapOrObjectSerializer : KSerializer<Map<String, Int>> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Map<String, Int>) {
        val jsonEncoder = encoder as JsonEncoder
        val jsonElement = if (value.isEmpty()) {
            JsonArray(emptyList())
        } else {
            JsonObject(value.mapValues { JsonPrimitive(it.value) })
        }
        jsonEncoder.encodeJsonElement(jsonElement)
    }

    override fun deserialize(decoder: Decoder): Map<String, Int> {
        val jsonDecoder = decoder as JsonDecoder
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonArray -> emptyMap()
            is JsonObject -> element.mapValues { it.value.jsonPrimitive.int }
            else -> throw SerializationException("Unexpected format for PiHoleTopQueries field")
        }
    }
}
