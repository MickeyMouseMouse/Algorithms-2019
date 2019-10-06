package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть позже первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    // Трудоемкость: O(n^2), где n - количество входных значений
    // Ресурсоемкость: O(n), где n - количество входных значений
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        List<Integer> input = new ArrayList<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext())
                input.add(Integer.parseInt(scanner.nextLine()));
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("File reading failed");
        }

        int max = 0;
        int first = 0;
        int second = 0;
        for (int i = 0; i < input.size() - 1; i++) {
            for (int j = i + 1; j < input.size(); j++) {
                int profit = input.get(j) - input.get(i);
                if (profit > max) {
                    max = profit;
                    first = i;
                    second = j;
                }
            }
        }

        return new Pair<>(first + 1, second + 1);
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     *
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */

    // Трудоемкость: O(menNumber)
    // Ресурсоемкость: O(menNumber)
    static public int josephTask(int menNumber, int choiceInterval) {
        int[] array = new int[menNumber];

        for (int i = 0; i < menNumber; i++)
            array[i] = i + 1;

        int answer = 0;
        int currentIndex = -1;
        for (int i = 0; i < menNumber; i++) {
            int j = 0;
            while (j < choiceInterval) {
                if (++currentIndex == menNumber) currentIndex = 0; // go to the beginning of the array
                if (array[currentIndex] != 0)
                    if (i == menNumber - 1) {
                        answer = array[currentIndex];
                        break;
                    } else
                        j++;
            }
            array[currentIndex] = 0; // 0 = deleted number
        }

        return answer;
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    // Трудоемкость: O(first.length() * second.length())
    // Ресурсоемкость: O(first.length() * second.length())
    static public String longestCommonSubstring(String first, String second) {
        int[][] matrix = new int[first.length()][second.length()];

        int max = 0;
        int index = 0;
        for (int i = 0; i < first.length(); i++)
            for (int j = 0; j < second.length(); j++)
                if (first.charAt(i) == second.charAt(j)) {
                    if (i == 0 || j == 0)
                        matrix[i][j] = 1;
                    else
                        matrix[i][j] = matrix[i - 1][j - 1] + 1;

                    if (matrix[i][j] > max) {
                        max = matrix[i][j];
                        index = i;
                    }
                }

        if (max == 0) return "";
        return first.substring(index - max + 1, index + 1);
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */

    // Трудоемкость: O(limit * ln(limit))
    // Ресурсоемкость: O(1)
    static public int calcPrimesNumber(int limit) {
        int answer = 0;
        if (limit <= 1) return answer;
        answer++; // 2 is prime
        for (int i = 3; i <= limit; i++) {
            if (i % 2 == 0) continue;
            boolean fl = true;
            for (int j = 3; j <= Math.sqrt(i); j += 2)
                if (i % j == 0) {
                    fl = false;
                    break;
                }
            if (fl) answer++;
        }

        return answer;
    }

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
