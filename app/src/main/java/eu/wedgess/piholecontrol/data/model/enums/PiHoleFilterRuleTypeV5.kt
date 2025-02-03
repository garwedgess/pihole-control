package eu.wedgess.piholecontrol.data.model.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

const val WILDCARD_REGEX_PREFIX = "(\\.|^)"
const val WILDCARD_REGEX_SUFFIX = "$"

@Serializable(PiHoleFilterRuleType.Companion.Serializer::class)
enum class PiHoleFilterRuleType(val key: Int, val value: String) {
    ALLOW(0, "white"),
    DENY(1, "black"),
    REGEX_ALLOW(2, "regex_white"),
    REGEX_DENY(3, "regex_black");

    companion object {

        operator fun get(key: Int): PiHoleFilterRuleType = checkNotNull(
            PiHoleFilterRuleType.entries.firstOrNull { it.key == key }
        ) { "No ${PiHoleFilterRuleType::class.simpleName} found for key: $key" }

        object Serializer : KSerializer<PiHoleFilterRuleType> {
            override val descriptor: SerialDescriptor =
                buildClassSerialDescriptor(requireNotNull(PiHoleFilterRuleType::class.simpleName))

            override fun deserialize(decoder: Decoder): PiHoleFilterRuleType {
                val key = decoder.decodeInt()
                return PiHoleFilterRuleType[key]
            }

            override fun serialize(encoder: Encoder, value: PiHoleFilterRuleType) {
                encoder.encodeInt(value.key)
            }
        }
    }
}
