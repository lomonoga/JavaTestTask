package naumen;

import java.io.*;
import java.util.*;

public class Naumen {
    public static void main(String[] args) throws Exception {
        new Naumen().determineNumberOfRequests();
    }

    /**
     * Вначале сохраняем порядок запросов и их частотность в словарь
     * После проходимся по запросам и записываем их в cash
     * Как только cash заполнился начинаем заменять значение в cash
     * Заменяем след образом:
     * 0. Берём из cash элементы, которые встретятся не больше раз, чем новый элемент (по частотности словаря)
     * 1. Проходимся по каждому выбранному элементу из cash и смотрим какой след из этих 2 элементов (элемент из cash и новый элемент)
     * встретится в запросе
     * 2. Если встретился новый, но заменяем значение в cash, если же элемент из cash, то оставляем как есть
     * Таким образом мы динамически меняем cash в зависимости от запросов и минимилизируем запросы к распределённой системе
     * @throws Exception Выбрасывает исключения при некорректной работе с файлами
     */
    public void determineNumberOfRequests() throws Exception {
        // Работаем с файлом
        var path = "C:\\Users\\trint\\Desktop\\javaNaumen\\src\\main\\java\\naumen";
        var file = new File(path, "input.txt");
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        //Создаём все что нам нужно (словари частотности, лист с запросами и hashset cash и др)
        var dictRequests = new HashMap<String, Integer>();
        var dictResponse = new HashMap<String, Integer>();
        ArrayList<String> listRequests;
        HashSet<String> hashSetCash;
        var scannerFile = new Scanner(file);
        var maxCash = 0;
        var requests = 0;
        var skipRequests = 0;

        // Пропускает первую строку и запоминаем размер кэша и колличество обращений
        try {
            var array = scannerFile.nextLine().split(" ");
            maxCash = Integer.parseInt(array[0]);
            requests =Integer.parseInt(array[1]);
            listRequests = new ArrayList<>(requests);
            hashSetCash = new HashSet<>(maxCash);
        } catch (Exception e) {
            throw new Exception(e);
        }

        // Проходим по строкам файла и записываем порядок и количество запросов
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

            if (hashSetCash.size() < maxCash) {
                if (!hashSetCash.contains(newRequest))
                    hashSetCash.add(newRequest);
                else
                    skipRequests++;
            }
            else {
            }

        }












/*        // Создаём лист и сортируем словарь по значению
        List<Map.Entry<String, Integer>> list = new ArrayList<>(dictRequests.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // Считаем сколько раз система будет обращаться
        for (var i = 0; i < maxCash; i++) {
            requests -= Math.max(list.get(i).getValue() - 1, 0);
        }*/




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
            fileWriter.write(Integer.toString(requests));
        }
    }
}
