package eu.wedgess.piholecontrol.data.utils.sqldelight

import app.cash.sqldelight.ColumnAdapter
import eu.wedgess.piholecontrol.data.model.enums.PiHoleApiVersionData
import io.ktor.http.URLProtocol
import java.util.UUID

val protocolAdapter = object : ColumnAdapter<URLProtocol, String> {
    override fun decode(databaseValue: String): URLProtocol =
        if (databaseValue == "http") URLProtocol.HTTP else URLProtocol.HTTPS

    override fun encode(value: URLProtocol): String = value.name
}

val portAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int = databaseValue.toInt()

    override fun encode(value: Int): Long = value.toLong()
}

val uuidAdapter = object : ColumnAdapter<UUID, String> {
    override fun decode(databaseValue: String): UUID = UUID.fromString(databaseValue)

    override fun encode(value: UUID): String = value.toString()
}

val apiVersionAdapter = object : ColumnAdapter<PiHoleApiVersionData, Long> {
    override fun decode(databaseValue: Long): PiHoleApiVersionData = PiHoleApiVersionData[databaseValue]

    override fun encode(value: PiHoleApiVersionData): Long = value.key
}
