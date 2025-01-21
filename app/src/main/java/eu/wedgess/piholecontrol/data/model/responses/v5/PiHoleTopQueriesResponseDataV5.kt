package eu.wedgess.piholecontrol.data.model.responses.v5

import eu.wedgess.piholecontrol.data.model.TopQueryData
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
import kotlin.math.floor

@Serializable
data class PiHoleTopQueriesResponseDataV5(
    @Serializable(with = MapOrObjectSerializer::class)
    @SerialName("top_ads")
    val topAds: Map<String, Int> = mapOf(),
    @Serializable(with = MapOrObjectSerializer::class)
    @SerialName("top_queries")
    val topQueries: Map<String, Int> = mapOf()
) {
    val topAdsPercentages: List<TopQueryData>
        get() = calculatePercentages(topAds)

    val topQueriesPercentages: List<TopQueryData>
        get() = calculatePercentages(topQueries)

    private fun calculatePercentages(data: Map<String, Int>): List<TopQueryData> {
        val globalTotal = data.values.sum()

        return data.map { (key, count) ->
            val percentage = if (globalTotal == 0) {
                0f
            } else {
                (count / globalTotal.toFloat()) * 100f
            }
            val truncatedPercentage = floor(percentage * 100) / 100
            TopQueryData(key, count, truncatedPercentage)
        }.sortedByDescending { it.percentage }
    }

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
}
