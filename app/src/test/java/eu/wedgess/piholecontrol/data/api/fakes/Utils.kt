package eu.wedgess.piholecontrol.data.api.fakes

import java.io.File

internal fun loadJson(directory: String, fileName: String): String {
    return File("src/test/resources/json/$directory/$fileName").readText(Charsets.UTF_8)
}
