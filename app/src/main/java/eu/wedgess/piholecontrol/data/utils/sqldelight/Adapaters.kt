package eu.wedgess.piholecontrol.data.utils.sqldelight

import app.cash.sqldelight.ColumnAdapter
import io.ktor.http.URLProtocol

val protocolAdapter = object : ColumnAdapter<URLProtocol, String> {
    override fun decode(databaseValue: String): URLProtocol =
        if (databaseValue == "http") URLProtocol.HTTP else URLProtocol.HTTPS

    override fun encode(value: URLProtocol): String = value.name
}

val portAdapter = object : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int = databaseValue.toInt()

    override fun encode(value: Int): Long = value.toLong()
}