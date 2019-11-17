package lesson3

import java.util.*
import kotlin.math.max

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {
    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {
        var right: Node<T>? = null
        var left: Node<T>? = null
        var parent: Node<T>? = null

        fun getMin(): Node<T> {
            var min = this
            while (min.left != null)
                min = min.left!!
            return min
        }
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
                newNode.parent = closest
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
                newNode.parent = closest
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    // Трудоемкость: O( lg(n) ), где n - "глубина" дерева
    // Ресурсоемкость: O(1)
    override fun remove(element: T): Boolean {
        val tree = find(element) ?: return false
        deleteNode(tree)
        return true
    }

    private fun deleteNode(element: Node<T>) {
        size--
        when {
            element.right == null -> change(element.left, element)
            element.left == null -> change(element.right, element)
            else -> {
                var minOnRight = element.right
                while (minOnRight!!.left != null)
                    minOnRight = minOnRight.left

                if (element != minOnRight.parent) {
                    change(minOnRight.right, minOnRight)
                    minOnRight.right = element.right
                    minOnRight.right!!.parent = minOnRight
                }

                change(minOnRight, element)
                minOnRight.left = element.left
                minOnRight.left!!.parent = minOnRight
            }
        }
    }

    private fun change(first: Node<T>?, second: Node<T>) {
        when {
            second.parent == null -> root = first
            second == second.parent!!.right -> second.parent!!.right = first
            else -> second.parent!!.left = first
        }

        if (first != null) first.parent = second.parent
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun getNextNode(element: Node<T>): Node<T>? {
        if (element.right != null) return element.right!!.getMin()

        var el = element // copy for modification
        var result = el.parent
        if (result == null) return result
        while (result!!.right == el) {
            el = result
            result = result.parent
            if (result == null) break
        }

        return result
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {
        private var currentNode: Node<T>? = null
        private var nextNode: Node<T>? = root?.getMin()

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        // Трудоемкость: O(1)
        // Ресурсоемкость: O(1)
        override fun hasNext() = nextNode != null

        /**
         * Поиск следующего элемента
         * Средняя
         */
        // Трудоемкость: O( lg(n) ), где n - "глубина" дерева
        // Ресурсоемкость: O(1)
        override fun next(): T {
            currentNode = nextNode
            nextNode = when {
                root == null -> null
                nextNode == null -> root!!.getMin()
                else -> getNextNode(nextNode!!)
            }

            return currentNode?.value ?: throw NoSuchElementException()
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        // Трудоемкость: O( lg(n) ), где n - "глубина" дерева
        // Ресурсоемкость: O(1)
        override fun remove() {
            currentNode?.let { deleteNode(it) } ?: throw NoSuchElementException()
            currentNode = null
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}