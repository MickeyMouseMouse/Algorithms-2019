package lesson1;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    // Трудоемкость: O(n^2), где n - количество входных строк
    // Ресурсоемкость: O(n), где n - количество входных строк

    static class Time implements Comparable<Time> {
        private byte hours, minutes, seconds;
        private boolean noon; // false = AM, true = PM

        private Time(String str) {
            if (!str.matches("([0-9]{2}:){2}[0-9]{2} (AM|PM)"))
                throw new IllegalArgumentException(str);

            String[] parts = str.split("[ :]");

            try {
                hours = Byte.parseByte(parts[0]);
                minutes = Byte.parseByte(parts[1]);
                seconds = Byte.parseByte(parts[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(str);
            }

            if ( !(hours >= 0 && hours <= 12) ||
                 !(minutes >= 0 && minutes <= 59) ||
                 !(seconds >= 0 && seconds <= 59) )
                throw new IllegalArgumentException(str);

            if (parts[3].equals("AM"))
                noon = false;
            else
                if (parts[3].equals("PM"))
                    noon = true;
            else
                throw new IllegalArgumentException(str);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();

            if (hours < 10) result.append("0");
            result.append(hours).append(":");
            if (minutes < 10) result.append("0");
            result.append(minutes).append(":");
            if (seconds < 10) result.append("0");
            result.append(seconds).append(" ");
            if (noon)
                result.append("PM");
            else
                result.append("AM");

            return result.toString();
        }

        @Override
        public int compareTo(@NotNull Time other) {
            if (!noon && other.noon) return -1;
            if (noon && !other.noon) return 1;

            Integer secThis = 3600 * (hours % 12) + 60 * minutes + seconds;
            Integer secOther = 3600 * (other.hours % 12) + 60 * other.minutes + other.seconds;
            return secThis.compareTo(secOther);
        }
    }

    static public void sortTimes(String inputName, String outputName) throws IOException {
        List<Time> input = new ArrayList<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext())
                input.add(new Time(scanner.nextLine()));
        }

        Collections.sort(input);

        // writing to output file
        try (FileWriter writer = new FileWriter(outputName)) {
            for (Time time : input)
                writer.write(time.toString() + "\n");
        }
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */

    // Трудоемкость: O(m * n)
    // Ресурсоемкость: O(m * n)
    //      m - количество различных адресов
    //      n - количество персон, проживающих по одному и тому же адресу (в среднем)

    static class Person implements Comparable<Person> {
        private String surname, name;

        public Person(String surname, String name) {
            this.surname = surname;
            this.name = name;
        }

        @Override
        public int compareTo(@NotNull Person other) {
            if (surname.compareTo(other.surname) < 0) return -1;
            if (surname.compareTo(other.surname) == 0) {
                if (name.compareTo(other.name) < 0) return -1;
                if (name.compareTo(other.name) == 0) return 0;
            }
            return 1;
        }
    }

    static class Address implements Comparable<Address> {
        private String streetName;
        private int number;

        private Address(String streetName, int number) {
            this.streetName = streetName;
            this.number = number;
        }

        @Override
        public int compareTo(@NotNull Address other) {
            if (streetName.compareTo(other.streetName) < 0) return -1;
            if (streetName.compareTo(other.streetName) == 0) {
                if (number < other.number) return -1;
                if (number == other.number) return 0;
            }
            return 1;
        }
    }

    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Map<Address, List<Person>> input = new TreeMap<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext()) {
                String str = scanner.nextLine();

                if (!str.matches("([А-яЁёA-z]+ ){2}- [А-яЁёA-z]+(-[А-яЁёA-z]+)* [0-9]+"))
                    throw new IllegalArgumentException();

                String[] parts = str.split(" ");

                Address currentAddress = new Address(parts[3], Integer.parseInt(parts[4]));
                List<Person> tmp = input.getOrDefault(currentAddress, new ArrayList<>());
                tmp.add(new Person(parts[0], parts[1]));
                input.put(currentAddress, tmp);
            }
        }

        for (List<Person> persons : input.values())
            Collections.sort(persons);

        // writing to output file
        try (FileWriter writer = new FileWriter(outputName)) {
            for (Map.Entry<Address, List<Person>> pair : input.entrySet()) {
                writer.write(pair.getKey().streetName + " " + pair.getKey().number + " - ");
                for (int i = 0; i < pair.getValue().size(); i++) {
                    writer.write(pair.getValue().get(i).surname + " ");
                    writer.write(pair.getValue().get(i).name);
                    if (i == pair.getValue().size() - 1)
                        writer.write("\n");
                    else
                        writer.write(", ");
                }
            }
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    // Трудоемкость: O(n), где n - количество входных строк
    // Ресурсоемкость: O(n), где n - количество входных строк
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        List<Integer> input = new ArrayList<>();
        int min = 0;

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext()) {
                String str = scanner.nextLine();

                if (!str.matches("(-|)[0-9]+.[0-9]"))
                    throw new IllegalArgumentException(str);

                int tmp = (int)(10 * Float.parseFloat(str));
                if (tmp < min) min = tmp;
                input.add(tmp);
            }
        }
        min = Math.abs(min);

        for (int i = 0; i < input.size(); i++)
            input.set(i, input.get(i) + min);

        int[] in = countingSort(input, 7730);

        for (int i = 0; i < in.length; i++)
            in[i] = in[i] - min;

        // writing to output file
        try (FileWriter writer = new FileWriter(outputName)) {
            for (int value : in) {
                if (value < 0) {
                    writer.write('-');
                    value = -value;
                }
                writer.write(value / 10 + "." + value % 10 + '\n');
            }
        }
    }

    private static int[] countingSort(List<Integer> elements, int limit) {
        int[] count = new int[limit + 1];
        for (int element: elements) {
            count[element]++;
        }
        for (int j = 1; j <= limit; j++) {
            count[j] += count[j - 1];
        }
        int[] out = new int[elements.size()];
        for (int j = elements.size() - 1; j >= 0; j--) {
            out[count[elements.get(j)] - 1] = elements.get(j);
            count[elements.get(j)]--;
        }
        return out;
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */

    // Трудоемкость: O(n), где n - количество входных значений
    // Ресурсоемкость: O(n), где n - количество входных значений
    static public void sortSequence(String inputName, String outputName) throws IOException {
        List<Integer> input = new ArrayList<>();
        Map<Integer, Integer> count = new HashMap<>();

        // reading from input file
        try (Scanner scanner = new Scanner(new File(inputName))) {
            while (scanner.hasNext()) {
                int number = Integer.parseInt(scanner.nextLine());
                input.add(number);
                count.put(number, count.getOrDefault(number, 0) + 1);
            }
        }

        int value = 0;
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet())
            if (entry.getValue() > max ||
                    (entry.getValue() == max && value > entry.getKey())) {
                value = entry.getKey();
                max = entry.getValue();
            }

        // writing to output file
        try (FileWriter writer = new FileWriter(outputName)) {
            for (Integer i : input)
                if (!i.equals(value)) writer.write(i + "\n");

            while (max-- > 0)
                writer.write(value + "\n");
        }
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 18 20 23 28]
     */

    // Трудоемкость: O(nln(n)), где n - количество входных значений
    // Ресурсоемкость: O(1)
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        int i = 0; // index for first
        int j = first.length; // index for second
        int z = 0; // index for "new" second

        while (z < second.length) {
            if (j >= second.length || (i < first.length && first[i].compareTo(second[j]) < 0))
                second[z++] = first[i++];
            else
                second[z++] = second[j++];
        }
    }
}
