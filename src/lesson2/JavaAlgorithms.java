package lesson2;

import kotlin.Pair;

import java.io.File;
import java.io.FileNotFoundException;
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

    // Трудоемкость: O(n), где n - количество входных значений
    // Ресурсоемкость: O(1)
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) throws FileNotFoundException {
        List<Integer> input = new ArrayList<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                if (!str.matches("[0-9]+")) throw new IllegalArgumentException();
                input.add(Integer.parseInt(str));
            }
        }

        int buy = 0;
        int sell = 0;
        int minCost = 0;
        for (int i = 1; i < input.size(); i++) {
            if (input.get(minCost) > input.get(i)) minCost = i;

            if (input.get(i) - input.get(minCost) > input.get(sell) - input.get(buy)) {
                buy = minCost;
                sell = i;
            }
        }

        return new Pair<>(buy + 1, sell + 1);
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
    // Ресурсоемкость: O(1)
    static public int josephTask(int menNumber, int choiceInterval) {
        int result = 1;

        for (int i = 2; i <= menNumber; i++)
            result = (choiceInterval + result - 1) % i + 1;

        return result;
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

    // Трудоемкость: O(limit * sqrt(limit))
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

    // Трудоемкость: O(i * j), где i и j размеры входной матрицы
    // Ресурсоемкость: O(i * j), где i и j размеры входной матрицы
    //      (длина и количество искомых слов также имеют значение)

    // field with letters (input data)
    private static class Data {
        private List<Character> inputField = new ArrayList<>();
        private int height = 0;
        private int width = 0;
    }

    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws FileNotFoundException {
        Data data = new Data();
        Set<String> answer = new HashSet<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
                if (!str.matches("([A-ZА-ЯЁ] )+[A-ZА-ЯЁ]"))
                    throw new IllegalArgumentException();
                data.height++;
                for (String ch : str.split(" "))
                    data.inputField.add(ch.charAt(0));
            }
        }
        data.width = data.inputField.size() / data.height;

        for (String str : words) {
            if (!str.matches("[A-ZА-ЯЁ]+"))
                throw new IllegalArgumentException();

            boolean done = false;
            for (int i = 0; i < data.height; i++) {
                for (int j = 0; j < data.width; j++)
                    if (data.inputField.get(i * data.width + j) == str.charAt(0)) {
                        Set<Pair<Integer, Integer>> busy = new HashSet<>();
                        busy.add(new Pair<>(i, j));

                        if (findWord(data, str.substring(1), i, j, busy)) {
                            answer.add(str);
                            done = true;
                            break;
                        }
                    }
                if (done) break;
            }
        }

        return answer;
    }

    // word = part of current word we are looking for
    // i, j = coordinates of current point in inputField
    // busy = characters which are busy already
    private static boolean findWord(Data data, String word, int i, int j, Set<Pair<Integer, Integer>> busy) {
        // up
        if (i != 0 && !busy.contains(new Pair<>(i - 1, j)))
            if (data.inputField.get((i - 1) * data.width + j) == word.charAt(0))
                if (word.length() == 1)
                    return true;
                else {
                    Set<Pair<Integer, Integer>> tmp = new HashSet<>(busy);
                    tmp.add(new Pair<>(i - 1, j));
                    if (findWord(data, word.substring(1), i - 1, j, tmp)) return true;
                }

        // right
        if (j != data.width - 1 && !busy.contains(new Pair<>(i, j + 1)))
            if (data.inputField.get(i * data.width + j + 1) == word.charAt(0))
                if (word.length() == 1)
                    return true;
                else {
                    Set<Pair<Integer, Integer>> tmp = new HashSet<>(busy);
                    tmp.add(new Pair<>(i, j + 1));
                    if (findWord(data, word.substring(1), i, j + 1, tmp)) return true;
                }

        // down
        if (i != data.height - 1 && !busy.contains(new Pair<>(i + 1, j)))
            if (data.inputField.get((i + 1) * data.width + j) == word.charAt(0))
                if (word.length() == 1)
                    return true;
                else {
                    Set<Pair<Integer, Integer>> tmp = new HashSet<>(busy);
                    tmp.add(new Pair<>(i + 1, j));
                    if (findWord(data, word.substring(1), i + 1, j, tmp)) return true;
                }

        // left
        if (j != 0 && !busy.contains(new Pair<>(i, j - 1)))
            if (data.inputField.get(i * data.width + j - 1) == word.charAt(0))
                if (word.length() == 1)
                    return true;
                else {
                    Set<Pair<Integer, Integer>> tmp = new HashSet<>(busy);
                    tmp.add(new Pair<>(i, j - 1));
                    return findWord(data, word.substring(1), i, j - 1, tmp);
                }

        return false;
    }
}
