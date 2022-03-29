package util

fun String.toDigitsOnly() = replace("""\D""".toRegex(), "")
