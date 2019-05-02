package io.github.fajran.kudupiye

typealias Data = MutableMap<String, Int>

fun Data.plus(data: Data) {
    data.forEach { key, value ->
        val curr = getOrDefault(key, 0)
        put(key, curr + value)
    }
}

fun Data.minus(data: Data) {
    data.forEach { key, value ->
        val curr = getOrDefault(key, 0)
        put(key, curr - value)
    }
}

fun Data.delta(data: Data): Data {
    val delta: Data = mutableMapOf()
    delta.plus(data)
    delta.minus(this)
    return delta
}

