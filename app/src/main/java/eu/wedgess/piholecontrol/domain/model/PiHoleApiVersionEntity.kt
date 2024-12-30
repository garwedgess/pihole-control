package eu.wedgess.piholecontrol.domain.model

enum class PiHoleApiVersionEntity(val key: Long) {
    Version5(1),
    Version6(2);

    companion object {
        operator fun get(key: Long): PiHoleApiVersionEntity = requireNotNull(
            PiHoleApiVersionEntity.entries.find { it.key == key },
            lazyMessage = {
                "Failed to find ${PiHoleApiVersionEntity::class.simpleName} for $key, possible values: " +
                        PiHoleApiVersionEntity.entries.joinToString { it.key.toString() }
            }
        )
    }
}