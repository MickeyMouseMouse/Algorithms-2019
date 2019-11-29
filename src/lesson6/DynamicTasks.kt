@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
// Трудоемкость: O(first.length * second.length)
// Ресурсоемкость: O(first.length * second.length)
fun longestCommonSubSequence(first: String, second: String): String {
    val matrix = Array(first.length) { Array(second.length) { "" } }

    fun getMaxString(numLine: Int, numColumn: Int): String {
        if (numLine == 0 || numColumn == 0) return ""

        var result = ""
        var max = 0
        for (i in 0 until numLine)
            for (j in 0 until numColumn)
                if (matrix[i][j].length > max) {
                    max = matrix[i][j].length
                    result = matrix[i][j]
                }

        return result
    }

    var answer = ""
    for (i in 0 until first.length)
        for (j in 0 until second.length)
            if (first[i] == second[j]) {
                matrix[i][j] = getMaxString(i, j) + first[i]
                if (matrix[i][j].length > answer.length) answer = matrix[i][j]
            }

    return answer
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    TODO()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
// Трудоемкость: O(n), где n - количество элементов заданного поля
// Ресурсоемкость: O(n), где n - количество элементов заданного поля
fun shortestPathOnField(inputName: String): Int {
    val input = File(inputName)
        .readLines()
        .map { it1 ->
            it1.split(" ")
                .map { it2 -> it2.toInt() }
        }

    var answer = 0
    fun findWays(i: Int, j: Int, sum: Int) {
        if (i == input.size - 1 && j == input[0].size - 1) {
            if (sum < answer || answer == 0) answer = sum
            return
        }

        val newSum = sum + input[i][j]
        if (newSum > answer && answer != 0) return

        if (i + 1 < input.size) findWays(i + 1, j, newSum)
        if (j + 1 < input[0].size) findWays(i, j + 1, newSum)
        if (i + 1 < input.size && j + 1 < input[0].size) findWays(i + 1, j + 1, newSum)
    }

    findWays(0, 0, 0)

    return answer
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5