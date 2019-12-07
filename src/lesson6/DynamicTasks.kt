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
// Ресурсоемкость: O(first.length * second.length)

// И трудоемкость, и ресурсоемкость зависят от того,
// как много будет совпадений во входных строках.
// Чем больше совпадений, тем чаще придется просматривать
// matrix на предмет наибольшей имеющейся строки и тем более
// длинные строки придется там хранить.
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
// Трудоемкость: O(list.size^2)
// Ресурсоемкость: O(list.size)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    when (list.size) {
        0 -> return listOf()
        1 -> return list
    }

    var answer = listOf<Int>()

    fun makeSequences(i: Int, seq: List<Int>) {
        for (j in i + 1 until list.size)
            if (seq[seq.size - 1] < list[j]) {
                val newSeq = seq.toMutableList()
                newSeq.add(list[j])
                makeSequences(j, newSeq)
            }

        if (seq.size > answer.size) answer = seq
    }

    for (i in 0 until list.size - 1)
        makeSequences(i, listOf(list[i]))

    return answer
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
        .map { line ->
            line.split(" ")
                .map { number -> number.toInt() }
        }

    val accumulationField = Array(input.size) { IntArray(input[0].size) { Int.MAX_VALUE } }
    accumulationField[0][0] = 0

    val visited = mutableSetOf<Pair<Int, Int>>()
    val next = mutableListOf<Pair<Int, Int>>()
    next.add(Pair(0, 0))

    fun fillMatrix(i: Int, j: Int) {
        next.removeAt(0)

        if (i + 1 < input.size)
            if (Pair(i + 1, j) !in visited)
                if (accumulationField[i][j] + input[i + 1][j] < accumulationField[i + 1][j]) {
                    accumulationField[i + 1][j] = accumulationField[i][j] + input[i + 1][j]
                    next.add(Pair(i + 1, j))
                }

        if (j + 1 < input[0].size)
            if (Pair(i, j + 1) !in visited)
                if (accumulationField[i][j] + input[i][j + 1] < accumulationField[i][j + 1]) {
                    accumulationField[i][j + 1] = accumulationField[i][j] + input[i][j + 1]
                    next.add(Pair(i, j + 1))
                }

        if (i + 1 < input.size && j + 1 < input[0].size)
            if (Pair(i + 1, j + 1) !in visited)
                if (accumulationField[i][j] + input[i + 1][j + 1] < accumulationField[i + 1][j + 1]) {
                    accumulationField[i + 1][j + 1] = accumulationField[i][j] + input[i + 1][j + 1]
                    next.add(Pair(i + 1, j + 1))
                }

        visited.add(Pair(i, j))
    }

    while (next.isNotEmpty())
        fillMatrix(next[0].first, next[0].second)

    return accumulationField[accumulationField.size - 1][accumulationField[0].size - 1]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5