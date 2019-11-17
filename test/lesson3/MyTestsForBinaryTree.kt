package lesson3

import kotlin.test.Test
import kotlin.test.assertEquals


class MyTestsForBinaryTree {
    @Test
    fun remove() {
        val tree = KtBinaryTree<Int>()
        tree.add(1)
        tree.add(2)
        tree.add(7)
        tree.add(10)
        tree.remove(2)

        val answer = KtBinaryTree<Int>()
        answer.add(1)
        answer.add(7)
        answer.add(10)

        assertEquals(answer, tree)
    }

    @Test
    fun iterator() {
        // creating a new tree
        val tree = KtBinaryTree<Int>()
        tree.add(5)
        tree.add(8)
        tree.add(7)
        tree.add(9)
        tree.add(3)
        tree.add(1)

        // test hasNext and next
        val tmp = mutableSetOf<Int>()
        var iterator = tree.iterator()
        while (iterator.hasNext())
            tmp.add(iterator.next())

        val answer = mutableSetOf(5, 8, 7, 9, 3, 1)
        assertEquals(answer, tmp)

        // test remove
        iterator = tree.iterator()
        while (iterator.hasNext())
            if (iterator.next() in 5..7) iterator.remove()

        tmp.clear()
        iterator = tree.iterator()
        while (iterator.hasNext())
            tmp.add(iterator.next())

        answer.remove(5)
        answer.remove(7)
        assertEquals(answer, tmp)
    }
}