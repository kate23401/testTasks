import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLHandler extends DefaultHandler {
    private String title, body, lastElementName;
    public static HashMap<String, String> parsedPair = new HashMap<>();

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
                title = information;
            if (lastElementName.equals("abstract"))
                body = information;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ((title != null && !title.isEmpty()) && (body != null && !body.isEmpty())) {
            parsedPair.put(title, body);        // Складываем ВСЕ узлы <title> и <abstarct> XML-файла в Map
            title = null;
            body = null;
        }
    }

    public ArrayList<String> checkMatchesInNode(String keyWord) {
        ArrayList<String> resultList = new ArrayList<>();
        String strPrn = "(.*?\\b)" + keyWord + "(\\b.*?)";
        Pattern pattern = Pattern.compile(strPrn, Pattern.CASE_INSENSITIVE);

        for (Map.Entry<String, String> map : parsedPair.entrySet()) {
            Matcher matcher = pattern.matcher(map.getValue());
            if (matcher.matches()) {
                resultList.add(map.getKey() + "\n" + map.getValue());
            }
        }

        return resultList;
    }
}
