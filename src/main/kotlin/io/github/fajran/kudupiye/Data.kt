package io.github.fajran.kudupiye

class Data(
        val values: MutableMap<String, Int> = mutableMapOf()
) {
    @Synchronized
    fun plus(data: Data) {
        data.values.forEach { key, value ->
            val curr = values.getOrDefault(key, 0)
            values[key] = curr + value
        }
    }

    @Synchronized
    fun minus(data: Data) {
        data.values.forEach { key, value ->
            val curr = values.getOrDefault(key, 0)
            values[key] = curr - value
        }
    }

    @Synchronized
    fun delta(data: Data): Data {
        val delta = Data(data.values.toMutableMap())
        delta.minus(this)
        return delta
    }
}

