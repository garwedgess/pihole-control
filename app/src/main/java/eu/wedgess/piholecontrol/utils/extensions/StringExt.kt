package eu.wedgess.piholecontrol.utils.extensions

fun String.isValidHost(): Boolean {
    val hostnameRegex = """^(([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.[a-zA-Z]{2,})$""".toRegex()
    val ipv4Regex = """^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]
        ||2[0-4][0-9]|[01]?[0-9][0-9]?)$
    """.trimMargin().toRegex()

    return this.matches(hostnameRegex) || this.matches(ipv4Regex)
}

fun String.isDigitsOnly(): Boolean {
    val digitsOnlyRegex = """^\d+$""".toRegex()
    return this.matches(digitsOnlyRegex)
}
