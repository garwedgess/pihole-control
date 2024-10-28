package eu.wedgess.mihole.data.model.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

const val WILDCARD_REGEX_PREFIX = "(\\.|^)"
const val WILDCARD_REGEX_SUFFIX = "$"

@Serializable(FilterRuleType.Companion.Serializer::class)
enum class FilterRuleType(val key: Int, val value: String) {
    ALLOW(0, "white"),
    BLOCK(1, "black"),
    REGEX_ALLOW(2, "regex_white"),
    REGEX_BLOCK(3, "regex_black");

    val isRegex: Boolean get() =  this == REGEX_ALLOW || this == REGEX_BLOCK

    companion object {

        operator fun get(key: Int): FilterRuleType = checkNotNull(
            FilterRuleType.entries.firstOrNull { it.key == key }
        ) { "No ${FilterRuleType::class.simpleName} found for key: $key" }

        object Serializer : KSerializer<FilterRuleType> {
            override val descriptor: SerialDescriptor =
                buildClassSerialDescriptor(requireNotNull(FilterRuleType::class.simpleName))

            override fun deserialize(decoder: Decoder): FilterRuleType {
                val key = decoder.decodeInt()
                return FilterRuleType[key]
            }

            override fun serialize(encoder: Encoder, value: FilterRuleType) {
                encoder.encodeInt(value.key)
            }
        }
    }
}