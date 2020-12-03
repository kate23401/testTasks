import java.io.*;
import java.time.LocalTime;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        // Вычитали файл при первом запуске программы в HashMap
        System.out.println("Вычитываем файл в HashMap - " + LocalTime.now() + "\n"
                + "Загрузка...");

        File file = new File("data/enwiki-latest-abstract.xml");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        // Складываем файл в HashMap. Поиск будет быстрым, но это будет занимать много памяти на период выполнения программы
        XMLHandler handler = new XMLHandler();
        parser.parse(file, handler);

        System.out.println("Файл записан в HashMap - " + LocalTime.now());
        System.out.println();

        if (file.exists()) {
            System.out.println("Введите слово для поиска в консоль." + "\n"
                    + "Чтобы завершить обработку, введите пустое значение или пробел.");

            // Вычитаем из консоли введенное слово
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String str = reader.readLine().trim();
                if (str.isEmpty()) {
                    System.out.println("Введено пустое значение, обработка завершается...");
                    break;
                }

                if (!handler.parsedPair.containsKey(str)) {
                    System.out.println("Не найдены совпадения по ключевому слову - " + str + ".\n" + "Измените текст запроса.");
                } else {
                    System.out.println("Поиск по запрашиваемому слову " + str + " запущен - " + LocalTime.now() + ".\n");
                    System.out.println(handler.parsedPair.get(str));
                }
            }
        } else {
            System.out.println("Файл с именем enwiki-latest-abstract.xml не найден в папке data!" + "\n"
                    + " Убедитесь, что файл лежит в директории.");
        }

        System.out.println("Завершение работы поиска слова - " + LocalTime.now());
        // Очищаем массив c файлом после завершения поиска
        handler.parsedPair.clear();
    }
}
