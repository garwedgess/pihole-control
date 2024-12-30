package eu.wedgess.piholecontrol.presentation.connections.modify.model;

import eu.wedgess.piholecontrol.R
import eu.wedgess.piholecontrol.utils.UiText

enum class PiHoleApiVersion(val key: Long, val label: UiText) {
    Version5(1, UiText.StringResource(R.string.label_api_version_5)),
    Version6(2, UiText.StringResource(R.string.label_api_version_6));

    companion object {
        operator fun get(key: Long): PiHoleApiVersion = requireNotNull(
            PiHoleApiVersion.entries.find { it.key == key },
            lazyMessage = {
                "Failed to find ${PiHoleApiVersion::class.simpleName} for $key,possible values: " +
                        PiHoleApiVersion.entries.joinToString { it.key.toString() }
            }
        )
    }
}