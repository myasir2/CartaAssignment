inline fun <reified T : Enum<*>> enumValueOrDefault(name: String, default: T): T =
    T::class.java.enumConstants.firstOrNull { it.name == name } ?: default
