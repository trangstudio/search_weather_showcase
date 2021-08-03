package core.util.scripting.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashSet;

public class XmlHelper {
    public static HashSet<String> getInnerXml(String xmlString, String tagName) {
        HashSet<String> elements = new HashSet<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();
            NodeList body = doc.getDocumentElement().getElementsByTagName(tagName);
            for (int i = 0; i < body.getLength(); i++) {
                Node node = body.item(i);
                DOMImplementationLS ls = (DOMImplementationLS) doc.getImplementation();
                LSSerializer ser = ls.createLSSerializer();
                String content = ser.writeToString(node);
                elements.add(content);
            }
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elements;
    }

    public static HashSet<String> getInnerText(String xmlString, String tagName) {
        HashSet<String> elements = new HashSet<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();
            NodeList body = doc.getDocumentElement().getElementsByTagName(tagName);
            for (int i = 0; i < body.getLength(); i++) {
                Node node = body.item(i);
                elements.add(node == null ? "" : node.getTextContent());
            }
            return elements;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elements;
    }

    public static String getInnerTextFirstElement(String xmlString, String tagName) {
        HashSet<String> elements = getInnerText(xmlString, tagName);
        if (elements == null || elements.isEmpty()) {
            return "";
        }
        return elements.iterator().next();
    }
}
