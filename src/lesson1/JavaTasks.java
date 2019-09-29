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
    static public void sortTimes(String inputName, String outputName) {
        class Time implements Comparable {
            private byte hours, minutes, seconds;
            private boolean noon; // false = AM, true = PM

            private Time(String str) {
                String[] parts = str.split("[ :]");

                if (parts.length != 4) throw new IllegalArgumentException(str);

                try {
                    hours = Byte.parseByte(parts[0]);
                    minutes = Byte.parseByte(parts[1]);
                    seconds = Byte.parseByte(parts[2]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(str);
                }

                if ( !(hours >= 0 && hours <= 12) ||
                     !(minutes >= 0 && minutes <= 59) ||
                     !(seconds >= 0 && seconds <= 59) ) throw new IllegalArgumentException(str);

                if (parts[3].equals("AM"))
                    noon = false;
                else
                    if (parts[3].equals("PM"))
                        noon = true;
                    else
                        throw new IllegalArgumentException(str);
            }

            // -1 => this < obj; 0 => this == obj; 1 => this > obj
            @Override
            public int compareTo(@NotNull Object obj) {
                if (this == obj) return 0;
                if (getClass() != obj.getClass()) throw new IllegalArgumentException();

                Time other = (Time) obj;
                if (!noon && other.noon) return -1;
                if (noon && !other.noon) return 1;

                Integer secThis = 3600 * (hours % 12) + 60 * minutes + seconds;
                Integer secOther = 3600 * (other.hours % 12) + 60 * other.minutes + other.seconds;
                return secThis.compareTo(secOther);
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
        }

        List<Time> input = new ArrayList<>();

        // reading from input file
        try {
            Scanner scanner = new Scanner(new File(inputName));
            while (scanner.hasNext())
                input.add(new Time(scanner.nextLine()));
            scanner.close();
        } catch (IOException e) {
            System.out.println("File open failed");
        }

        input.sort(Time::compareTo);

        // writing to output file
        try {
            FileWriter writer = new FileWriter(outputName);
            for (Time time : input)
                writer.write(time.toString() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("File create failed");
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
    static public void sortAddresses(String inputName, String outputName) {
        class Person implements Comparable {
            private String surname, name;

            public Person(String surname, String name) {
                this.surname = surname;
                this.name = name;
            }

            // -1 => this < obj; 0 => this == obj; 1 => this > obj
            @Override
            public int compareTo(@NotNull Object obj) {
                if (this == obj) return 0;
                if (getClass() != obj.getClass()) throw new IllegalArgumentException();

                Person other = (Person) obj;
                if (surname.compareTo(other.surname) < 0) return -1;
                if (surname.compareTo(other.surname) == 0)
                    if (name.compareTo(other.name) <= 0) return -1;

                return 1;
            }
        }

        class Address implements Comparable {
            private String address;
            private List<Person> persons = new ArrayList<>();

            private Address(String streetName, int number) {
                address = streetName + " " + number;
            }

            private boolean isThisAddress(String streetName, int number) {
                return address.equals(streetName + " " + number);
            }

            private void addPerson(String surname, String name) {
                persons.add(new Person(surname, name));
            }

            private void sortPersons() { persons.sort(Person::compareTo); }

            // -1 => this < obj; 0 => this == obj; 1 => this > obj
            @Override
            public int compareTo(@NotNull Object obj) {
                if (this == obj) return 0;
                if (getClass() != obj.getClass()) throw new IllegalArgumentException();

                Address other = (Address) obj;
                return address.compareTo(other.address);
            }
        }

        List<Address> input = new ArrayList<>();

        // reading from input file
        try {
            Scanner scanner = new Scanner(new File(inputName));
            while (scanner.hasNext()) {
                String tmp = scanner.nextLine();

                if (!tmp.matches("([А-я|Ёё]+ ){2}- [А-я|Ёё]+(-[А-я|Ёё]+)* [0-9]+"))
                    throw new IllegalArgumentException();

                String[] parts = tmp.split(" ");

                boolean fl = true;
                int number = Integer.parseInt(parts[4]);
                for (Address adr : input)
                    if (adr.isThisAddress(parts[3], number)) {
                        fl = false;
                        adr.addPerson(parts[0], parts[1]);
                    }

                if (fl) {
                    input.add(new Address(parts[3], number));
                    input.get(input.size() - 1).addPerson(parts[0], parts[1]);
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("File open failed");
        }

        input.sort(Address::compareTo);
        for (Address adr : input)
            adr.sortPersons();

        // writing to output file
        try {
            FileWriter writer = new FileWriter(outputName);
            for (Address adr : input) {
                writer.write(adr.address + " - ");
                for (int i = 0; i < adr.persons.size(); i++) {
                    writer.write(adr.persons.get(i).surname + " ");
                    writer.write(adr.persons.get(i).name);
                    if (i == adr.persons.size() - 1)
                        writer.write("\n");
                    else
                        writer.write(", ");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("File create failed");
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
    static public void sortTemperatures(String inputName, String outputName) {
        List<Float> input = new ArrayList<>();

        // reading from input file
        try {
            Scanner scanner = new Scanner(new File(inputName));
            while (scanner.hasNext())
                input.add(Float.parseFloat(scanner.nextLine()));
            scanner.close();
        } catch (IOException e) {
            System.out.println("File open failed");
        }

        Collections.sort(input);

        // writing to output file
        try {
            FileWriter writer = new FileWriter(outputName);
            for (float i : input)
                writer.write(i + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("File create failed");
        }
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
    static public void sortSequence(String inputName, String outputName) {
        List<Integer> input = new ArrayList<>();
        Map<Integer, Integer> count = new HashMap<>();

        // reading from input file
        try {
            Scanner scanner = new Scanner(new File(inputName));
            while (scanner.hasNext()) {
                int number = Integer.parseInt(scanner.nextLine());
                input.add(number);

                if (count.containsKey(number))
                    count.put(number, count.get(number) + 1);
                else
                    count.put(number, 1);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("File open failed");
        }

        int value = 0;
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > max ||
                    (entry.getValue() == max && value > entry.getKey())) {
                value = entry.getKey();
                max = entry.getValue();
            }
        }

        // writing to output file
        try {
            FileWriter writer = new FileWriter(outputName);

            for (Integer i : input)
                if (!i.equals(value)) writer.write(i + "\n");

            while (max-- > 0)
                writer.write(value + "\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("File create failed");
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
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        int i = 0; // index for first
        int j = first.length; // index for second
        int z = 0; // index for "new" second

        // merge sort: O(Nln(N))
        while (z < second.length) {
            if (j >= second.length || (i < first.length && first[i].compareTo(second[j]) < 0))
                second[z++] = first[i++];
            else
                second[z++] = second[j++];
        }
    }
}
