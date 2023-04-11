package naumen;

import naumen.Exceptions.EmptyFileException;

import java.io.*;
import java.util.*;

public class Naumen {
    public static void main(String[] args) throws Exception {
        new Naumen().determineNumberOfRequests(Boolean.FALSE,
                "src/main/java/naumen", "input.txt");
    }

    /**
     * Вначале сохраняем порядок запросов и их частотность в словарь
     * После проходимся по запросам и записываем их в cash
     * Как только cash заполнился начинаем заменять значение в cash
     * Заменяем след образом:
     * 0. Находим след такой же запрос, если его нет, то cash не меняем, тк запросов таких больше не будет
     * 1. Проверяем, если cash будет задействован весь до того, как мы дойдём до этого элемента, то ничего не делаем, тк
     * это не изменит кол-во обращений к системе.
     * Если задействован не весь, то берём элемент из cash и перезаписываем его на новый след образом:
     * Выбираем элементы которые не будут задействованы и среди них берём тот, который встретится дальше всех (проверяем с конца)
     * 2. В каждой итерации подсчитываем сколько раз обращались к cash, для дальнейшего ответа на вопрос
     * Таким образом мы динамически меняем cash в зависимости от запросов и минимализируем запросы к распределённой системе
     * @throws Exception Выбрасывает исключения при некорректной работе с файлами
     * @param test Флаг, обозначающий вызов функции для теста или нет
     * @param path Путь для записи и чтения файлов, по условию он пуст
     * @return Значение, находящиеся в файле
     */
    public Integer determineNumberOfRequests(Boolean test, String path, String nameFile) throws Exception {
        //Подключение к файлу
        var file = new File(path, nameFile);
        if (!file.exists()) throw new FileNotFoundException();

        //Создаём все что нам нужно
        ArrayList<String> listRequests;
        HashSet<String> cash;
        HashMap<String, Integer> dictRequests;
        HashMap<String, Integer> dictResponse;
        var scannerFile = new Scanner(file);
        var maxCash = 0;
        var requests = 0;
        var skipRequests = 0;

        // Обрабатываем первую строку и запоминаем размер кэша и колличество обращений
        try {
            var array = scannerFile.nextLine().split(" ");
            maxCash = Integer.parseInt(array[0]);
            requests =Integer.parseInt(array[1]);
            listRequests = new ArrayList<>(requests);
            dictRequests = new HashMap<>();
            dictResponse = new HashMap<>();
            cash = new HashSet<>(maxCash);
        } catch (Exception e) {
            throw new EmptyFileException();
        }

        // Проходим по строкам файла и записываем порядок запросов и количество каждого запроса
        while (scannerFile.hasNextLine()) {
            var item = scannerFile.nextLine();
            listRequests.add(item);
            if (dictRequests.containsKey(item)) {
                dictRequests.replace(item, dictRequests.get(item) + 1);
            } else {
                dictRequests.put(item, 1);
            }
        }
        //Закрываем соединение с файлом
        scannerFile.close();
        if (maxCash == 0) return requests;
        //Реализация алгоритма, описанного в javaDoc
        for (var i = 0; i < requests; i++) {
            var newRequest = listRequests.get(i);
            if (dictResponse.containsKey(newRequest)) {
                dictResponse.replace(newRequest, dictResponse.get(newRequest) + 1);
            } else {
                dictResponse.put(newRequest, 1);
            }
            if (cash.contains(newRequest)) {
                skipRequests++;
                continue;
            }
            if (cash.size() < maxCash) cash.add(newRequest);
            else {
                var index = -1;
                if (dictRequests.get(newRequest) - dictResponse.get(newRequest) < 1) continue;
                for (var rangeIndex = i + 1; rangeIndex < requests; rangeIndex++) {
                    if (Objects.equals(listRequests.get(rangeIndex), newRequest)) {
                        index = rangeIndex;
                        break;
                    }
                }
                if (index == -1) continue;
                var isEnd = false;
                var itemForCash = "";
                var cashToChange = new HashSet<String>();
                for (String cashItem: cash) {
                    if (isEnd) break;
                    for (var indexCash = i; indexCash < index; indexCash++) {
                        if (Objects.equals(cashItem, listRequests.get(indexCash))) break;
                        if (dictRequests.get(cashItem) - dictResponse.get(cashItem) < 1) {
                            itemForCash = cashItem;
                            isEnd = true;
                            break;
                        } else {
                            cashToChange.add(cashItem);
                        }
                    }
                }
                if (cashToChange.isEmpty()) {
                    cash.remove(itemForCash);
                    cash.add(newRequest);
                    continue;
                }
                for (var endIndex = requests - 1; endIndex > index; --endIndex) {
                    if (cashToChange.contains(listRequests.get(endIndex))) {
                        cash.remove(listRequests.get(endIndex));
                        cash.add(newRequest);
                        break;
                    }
                }
            }
        }

       if (Boolean.FALSE.equals(test))
            makeFileWithResult(requests - skipRequests, path);

        return requests - skipRequests;
    }

    /**
     *
     * @param value Значение для записи в файл
     * @param path Путь для записи
     * @throws Exception Выбрасывает исключения при некорректной работе с файлами
     */
    private void makeFileWithResult(Integer value, String path) throws Exception {
        // Создаём файл и записываем туда результат
        var outputFile = new File(path, "output.txt");
        try {
            if (!outputFile.createNewFile()) {
                throw new Exception("Такой файл уже был создан");
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        try (FileWriter fileWriter = new FileWriter(outputFile)) {
            fileWriter.write(Integer.toString(value));
        }
    }

    /***
     * Алгоритм очень прожорливый, зато кол-во обращений к системе минимально
     * В реальном проекте я бы просто считал кол-во запросов и каких больше те бы и сохранял в cash
     * Это бы ело память меньше и работало бы быстрее, но и нагрузка на распределённую систему увеличилась
     */
}
