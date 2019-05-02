package io.github.fajran.kudupiye

data class Node(
        val id: Int,
        val parent: Node? = null,
        val children: Set<Node> = setOf(),
        val data: Data = Data(mutableMapOf())
) {
    val isLeafNode = children.isEmpty()

    @Synchronized
    fun set(data: Data): Node {
        if (!isLeafNode)
            throw IllegalStateException("Cannot set data to non-leaf nodes")

        val delta = this.data.delta(data)

        var node: Node? = this
        while (node != null) {
            node.data.plus(delta)
            node = node.parent
        }

        return this
    }
}