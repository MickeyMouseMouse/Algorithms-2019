package lesson3

import java.util.*
import kotlin.NoSuchElementException

class Trie : AbstractMutableSet<String>(), MutableSet<String> {
    override var size: Int = 0
        private set

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     * Сложная
     */
    override fun iterator(): MutableIterator<String> {
        return Iterator()
    }

    private inner class Iterator : MutableIterator<String> {
        val strings = LinkedList(listOf(""))
        val nodes = LinkedList(listOf(root))
        var current: String? = null

        // Трудоемкость: O(1)
        // Ресурсоемкость: O(1)
        override fun hasNext(): Boolean {
            return nodes.isNotEmpty()
        }

        // Трудоемкость: O(n), n - количество узлов (nodes)
        // Ресурсоемкость: O(1)
        override fun next(): String {
            while (nodes.isNotEmpty()) {
                current = strings.removeLast() // remove the last item from LinkedList and return it
                for (pair in nodes.removeLast().children)
                    if (pair.key != 0.toChar()) {
                        strings.add(current + pair.key)
                        nodes.add(pair.value)
                    } else return current!!
            }

            throw NoSuchElementException()
        }

        override fun remove() {
            TODO()
        }
    }
}