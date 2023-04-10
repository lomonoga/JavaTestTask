package naumen;

import java.io.*;
import java.util.*;

public class Naumen {
    public static void main(String[] args) throws Exception {
        new Naumen().determineNumberOfRequests(Boolean.FALSE,
                "C:\\Users\\trint\\Desktop\\javaNaumen\\src\\main\\java\\naumen");
    }

    /**
     * Вначале сохраняем порядок запросов и их частотность в словарь
     * После проходимся по запросам и записываем их в cash
     * Как только cash заполнился начинаем заменять значение в cash
     * Заменяем след образом:
     * 0. Находим след такой же запрос, если его нет, то cash не меняем, тк запросов таких больше не будет
     * 1. Проверяем, если cash будет задействован весь до того, как мы дойдём до этого элемента, то ничего не делаем, тк
     * это не изменит кол-во обращений к системе.
     * Если задействован не весь, то берём элемент из cash и перезаписываем его на новый
     * 2. В каждой итерации подсчитываем сколько раз обращались к cash, для дальнейшего ответа на вопрос
     * Таким образом мы динамически меняем cash в зависимости от запросов и минимализируем запросы к распределённой системе
     * @throws Exception Выбрасывает исключения при некорректной работе с файлами
     * @param test
     * @param path Путь для записи и чтения файлов, по условию он пуст
     */
    public void determineNumberOfRequests(Boolean test, String path) throws Exception {
        //Подключение к файлу
        var file = new File(path, "input.txt");
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
            throw new Exception(e);
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

        //Реализация алгоритма, описанного в javaDoc
        for (var i = 0; i < requests; i++) {
            var newRequest = listRequests.get(i);
            if (dictResponse.containsKey(newRequest)) {
                dictResponse.replace(newRequest, dictResponse.get(newRequest) + 1);
            } else {
                dictResponse.put(newRequest, 1);
            }
            if (cash.contains(newRequest)) skipRequests++;
            if (cash.size() < maxCash) cash.add(newRequest);
            else {
                var index = -1;
                if (dictRequests.get(newRequest) - dictResponse.get(newRequest) < 1) continue;
                for (var rangeIndex = i + 1; rangeIndex < requests; rangeIndex++) {
                    if (listRequests.get(rangeIndex) == newRequest) {
                        index = rangeIndex;
                        break;
                    }
                }
                for (String cashItem: cash) {
                    for (var indexCash = i; indexCash < index; indexCash++) {
                        if (cashItem == listRequests.get(indexCash)) break;
                        cash.remove(cashItem);
                        cash.add(newRequest);
                    }
                }
            }
        }

        if (Boolean.FALSE.equals(test))
            makeFileWithResult(requests - skipRequests, path);
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
}
