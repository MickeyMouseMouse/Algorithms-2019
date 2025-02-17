package lesson3

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class TrieTest {

    @Test
    @Tag("Example")
    fun generalTest() {
        val trie = Trie()
        assertEquals(0, trie.size)
        assertFalse("Some" in trie)
        trie.add("abcdefg")
        assertTrue("abcdefg" in trie)
        assertFalse("abcdef" in trie)
        assertFalse("a" in trie)
        assertFalse("g" in trie)

        trie.add("zyx")
        trie.add("zwv")
        trie.add("zyt")
        trie.add("abcde")
        assertEquals(5, trie.size)
        assertTrue("abcdefg" in trie)
        assertFalse("abcdef" in trie)
        assertTrue("abcde" in trie)
        assertTrue("zyx" in trie)
        assertTrue("zyt" in trie)
        assertTrue("zwv" in trie)
        assertFalse("zy" in trie)
        assertFalse("zv" in trie)

        trie.remove("zwv")
        trie.remove("zy")
        assertEquals(4, trie.size)
        assertTrue("zyt" in trie)
        assertFalse("zwv" in trie)

        trie.clear()
        assertEquals(0, trie.size)
        assertFalse("zyx" in trie)
    }

    @Test
    @Tag("Hard")
    fun rudeIteratorTest() {
        val trie = Trie()
        assertEquals(setOf<String>(), trie)
        trie.add("abcdefg")
        trie.add("zyx")
        trie.add("zwv")
        trie.add("zyt")
        trie.add("abcde")

        assertEquals(setOf("abcdefg", "zyx", "zwv", "zyt", "abcde"), trie)
    }

    // my tests
    @Test
    @Tag("Hard")
    fun treeTest() {
        val trie = Trie()
        trie.add("mickey")
        trie.add("mouse")
        trie.add("miss")
        trie.add("me")

        assertEquals(4, trie.size)
        assertTrue("mouse" in trie)
        assertFalse("micke" in trie)

        trie.remove("mouse")

        assertFalse("mouse" in trie)
        assertFalse("?" in trie)

        trie.clear()

        assertEquals(0, trie.size)
    }

    @Test
    @Tag("Hard")
    fun iteratorTest() {
        val trie = Trie()
        trie.add("0")
        trie.add("1")
        trie.add("12")
        trie.add("3")
        trie.add("345")

        val iterator = trie.iterator()
        assertTrue(iterator.hasNext())
        iterator.next()
    }
}