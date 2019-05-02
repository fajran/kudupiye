package io.github.fajran.kudupiye

class Aggregate(
        private val nodes: Map<Int, Node>
) {
    operator fun set(id: Int, data: Data): Data? = nodes[id]?.set(data)?.data

    operator fun get(id: Int): Data? = nodes[id]?.data
}