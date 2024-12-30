package eu.wedgess.piholecontrol.data.model.enums

enum class PiHoleApiVersionData(val key: Long) {
    Version5(1),
    Version6(2);

    companion object {
        operator fun get(key: Long): PiHoleApiVersionData = requireNotNull(
            PiHoleApiVersionData.entries.find { it.key == key },
            lazyMessage = {
                "Failed to find ${PiHoleApiVersionData::class.simpleName} for $key, possible values: " +
                        PiHoleApiVersionData.entries.joinToString { it.key.toString() }
            }
        )
    }
}