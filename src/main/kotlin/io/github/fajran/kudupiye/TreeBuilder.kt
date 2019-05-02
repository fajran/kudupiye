package io.github.fajran.kudupiye

class TreeBuilder {
    private val nodes = mutableMapOf<Int, Node>()

    fun add(id: Int, childId: Int): TreeBuilder {
        val node = nodes.getOrPut(id) { Node(id) }
        val childNode = nodes.getOrPut(childId) { Node(childId) }
        if (childNode.parent != null && childNode.parent.id != id)
            throw IllegalStateException("Cannot reparent a node id=$childId")

        nodes[id] = node.copy(children = node.children.plus(childNode))
        nodes[childId] = childNode.copy(parent = node)

        return this
    }

    fun build(): Map<Int, Node> {
        val roots = nodes.values.filter { it.parent == null }
        if (roots.size != 1)
            throw IllegalStateException("Found ${roots.size} root nodes")

        return nodes
    }
}