import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class XMLHandler extends DefaultHandler {
    private String titleNode, abstractNode, lastElementName;
    public static HashMap<String, ArrayList<String>> parsedPair = new HashMap<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        lastElementName = qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String information = new String(ch, start, length);

        information = information.replace("\n", "").trim();

        if (!information.isEmpty()) {
            if (lastElementName.equals("title"))
                titleNode = information;
            if (lastElementName.equals("abstract"))
                abstractNode = information;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ((titleNode != null && !titleNode.isEmpty()) && (abstractNode != null && !abstractNode.isEmpty())) {
            String str = "\n" + titleNode + "\n" + abstractNode + "\n";

            // Разбиваем строку  узла <abstract> на слова для ключей в HashMap
            String[] arr = abstractNode.replace("\\W", "").split(" ");
            //Чтобы исключить двойную вставку в значение HashMap. Например: A sister group or sister taxon is a phylogenetic term... - два раза повторяется sister
            arr = Arrays.asList(arr).stream().distinct().toArray(String[]::new);

            // Складываем ВСЕ узлы <title> и <abstract> XML-файла в HashMap
            for (String s : arr) {
                // Если слова из строки узла <abstract> нет в ключах HashMap, то добавляем такой ключ-слово + инициализируем значение HashMap созданием первой записи в ArrayList
                if (!parsedPair.containsKey(s)) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(str);
                    parsedPair.put(s, arrayList);
                } else { //Если слово из строки узла <abstract> есть в HashMap, то находим по ключу значение  - список ArrayList, добавляем в него вторую или последующуюю запись.
                    parsedPair.get(s).add(str);
                }
            }
            titleNode = null;
            abstractNode = null;
        }
    }
}
