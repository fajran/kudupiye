package io.github.fajran.kudupiye

class Aggregate(
        private val nodes: Map<Int, Node>
) {
    operator fun set(id: Int, data: Data): Node? = nodes[id]?.set(data)

    operator fun get(id: Int): Node? = nodes[id]
}