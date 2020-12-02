import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLHandler extends DefaultHandler {
    private String title, body, parseString, lastElementName;
    public static HashMap<String, String> parsedPair = new HashMap<>();

    XMLHandler(String str) {
        parseString = str;
    }

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
                if (checkMatchesInNode(information)) {
                    body = information;
                } else {
                    title = null;   // Зачистим узел с заголовком
                }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ((title != null && !title.isEmpty()) && (body != null && !body.isEmpty())) {
            parsedPair.put(title, body);
            title = null;
            body = null;
        }
    }

    private boolean checkMatchesInNode(String information) {
        String strPrn = "(\\s|-)?" + parseString + "(\\s|-)?";      // Маска либо на простое слово, либо на часть составного (Пример: self-made)
        Pattern pattern = Pattern.compile(strPrn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(information);
        return matcher.find();
    }
}
