import java.io.*;
import java.util.Map;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        System.out.println("Введите слово для поиска в консоль." + "\n"
                + "Чтобы завершить обработку, введите пустое значение или пробел." + "\n"
                + "------->");
        // Вычитаем из консоли введенное слово
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String str = reader.readLine().trim();
            if (str.isEmpty()) {
                System.out.println("Введено пустое значение, обработка завершается...");
                break;
            }
            // Распарсим файл с учетом переданного из консоли слова
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler(str);       // Передаем в конструктор слово, полученное из консоли
            parser.parse(new File("data/enwiki-latest-abstract.xml"), handler); // Имена файлов: enwiki-latest-abstract, fileXML

            // Проверим нашли ли вхождение слова из консоли
            if (handler.parsedPair.isEmpty()) {
                System.out.println("Не найдены совпадения по ключевому слову - " + str + ".\n" + "Измените текст запроса.");
                continue;   // Переходим на новую итерацию цикла.
            } else {
                System.out.println("Найдены совпадения по ключевому слову - " + str);
            }
            // Выведем найденные пары title-abstract
            for (Map.Entry<String, String> stringStringEntry : handler.parsedPair.entrySet()) {
                System.out.println(stringStringEntry.getKey() + "\n" + stringStringEntry.getValue());
            }
            // Очищаем выборку, чтобы данные не анализировались в следующей итерации
            handler.parsedPair.clear();
        }
    }
}
