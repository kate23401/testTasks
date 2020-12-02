import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        ArrayList<String> resultList = new ArrayList<>();

        // Вычитали файл при первом запуске программы в HashMap
        System.out.println("Вычитываем файл в HashMap - " + LocalTime.now() + "\n"
                + "Дождитесь окончания записи файла.");

        File file = new File("data/enwiki-latest-abstract.xml");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        // Складываем файл в HashMap, где ключ title, значение - abstract
        XMLHandler handler = new XMLHandler();
        parser.parse(file, handler);

        System.out.println("Файл записан в HashMap - " + LocalTime.now());
        System.out.println();

        if (file.exists()) {
            System.out.println("Введите слово для поиска в консоль." + "\n"
                    + "Чтобы завершить обработку, введите пустое значение или пробел." + "\n");

            // Вычитаем из консоли введенное слово
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String str = reader.readLine().trim();
                if (str.isEmpty()) {
                    System.out.println("Введено пустое значение, обработка завершается...");
                    break;
                }

                System.out.println("Поиск совпадений по введенному слову запущен - " + LocalTime.now());
                resultList = handler.checkMatchesInNode(str);
                System.out.println("Поиск совпадений по введенному слову завершен - " + LocalTime.now());

                // Проверим нашли ли вхождение слова из консоли
                if (resultList.isEmpty()) {
                    System.out.println("Не найдены совпадения по ключевому слову - " + str + ".\n" + "Измените текст запроса.");
                    continue;   // Переходим на новую итерацию цикла.
                } else {
                    System.out.println("Найдены совпадения по ключевому слову - " + str);
                }

                // Выведем найденные пары title-abstract
                resultList.forEach((k) -> System.out.println(k + "\n"));
                // Очищаем выборку, чтобы данные не анализировались в следующей итерации
                resultList.clear();

            }
        } else {
            System.out.println("Файл с именем не найден в папке data!" + "\n"
                    + " Убедитесь, что файл с именем лежит в директории.");
        }

        System.out.println("Завершение работы поиска слова - " + LocalTime.now());
        // Очищаем массив c файлом после завершения поиска
        handler.parsedPair.clear();
    }
}
