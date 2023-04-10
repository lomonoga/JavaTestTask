package naumen;

import java.io.*;
import java.util.*;

public class Naumen {
    public static void main(String[] args) throws Exception {
        new Naumen().determineNumberOfRequests();
    }

    /**
     * Создаёт файл с числом обращений к распределённой системе
     * @throws Exception Выбрасывает исключения при некорректной работе с файлами
     */
    public void determineNumberOfRequests() throws Exception {
        var path = "C:\\Users\\trint\\Desktop\\javaNaumen\\src\\main\\java\\naumen";
        var file = new File(path, "input.txt");
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        var dictRequests = new HashMap<String, Integer>();
        var scannerFile = new Scanner(file);
        var cash = 0;
        var call = 0;

        // Пропускает первую строку и запоминаем размер кэша и колличество обращений
        try {
            var array = scannerFile.nextLine().split(" ");
            cash = Integer.parseInt(array[0]);
            call =Integer.parseInt(array[1]);
        } catch (Exception e) {
            throw new Exception(e);
        }

        // Проходим по строкам файла и записываем сколько раз запросы в них повторяются
        while (scannerFile.hasNextLine()) {
            var item = scannerFile.nextLine();
            if (dictRequests.containsKey(item)) {
                dictRequests.replace(item, dictRequests.get(item) + 1);
            } else {
                dictRequests.put(item, 1);
            }
        }
        //Закрываем соединение
        scannerFile.close();

        // Создаём лист и сортируем словарь по значению
        List<Map.Entry<String, Integer>> list = new ArrayList<>(dictRequests.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        // Считаем сколько раз система будет обращаться
        for (var i = 0; i < cash; i++) {
            call -= Math.max(list.get(i).getValue() - 1, 0);
        }

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
            fileWriter.write(Integer.toString(call));
        }
    }
}
