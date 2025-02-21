package file_exporters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class XmlToCsvConverter {
    public static void main(String[] args) {
        String xmlFile = "src//main//files//data.xml";  // Ruta del archivo XML
        String csvFile = "src//main//files//outputXML.csv";  // Ruta de salida para el archivo CSV
        convertXmlToCsv(xmlFile, csvFile);
    }

    public static void convertXmlToCsv(String xmlFile, String csvFile) {
        try (FileWriter fw = new FileWriter(csvFile)) {
            // Crear un parser de XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFile));
            doc.getDocumentElement().normalize();

            // Obtener el nodo raíz del XML
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            // Usar un Set para asegurar que las cabeceras del CSV no tengan duplicados
            Set<String> headersSet = new HashSet<>();
            List<Map<String, String>> rowData = new ArrayList<>();

            // Recorrer los nodos
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Map<String, String> rowMap = new LinkedHashMap<>();
                    traverseElement(element, rowMap, "", headersSet);
                    rowData.add(rowMap);
                }
            }

            // Escribir la cabecera (sin duplicados)
            String[] headers = headersSet.toArray(new String[0]);
            fw.append(String.join(",", headers)).append("\n");

            // Escribir los datos en el archivo CSV
            for (Map<String, String> rowMap : rowData) {
                for (String header : headers) {
                    String value = rowMap.getOrDefault(header, "N/A");
                    fw.append(value).append(",");
                }
                fw.append("\n");
            }

            System.out.println("Archivo CSV generado: " + csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Función recursiva para recorrer cada elemento del XML y almacenar sus datos
    private static void traverseElement(Element element, Map<String, String> rowMap, String parentKey, Set<String> headersSet) {
        NodeList childNodes = element.getChildNodes();

        // Recorrer los nodos hijos
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                String key = parentKey + childElement.getTagName();

                // Si el hijo tiene un valor (es texto), lo agregamos al mapa
                if (childElement.getChildNodes().getLength() == 1 && childElement.getFirstChild().getNodeType() == Node.TEXT_NODE) {
                    String value = childElement.getTextContent().trim();
                    rowMap.put(key, value);
                    headersSet.add(key);  // Añadir la clave a las cabeceras
                } else {
                    // Si el hijo tiene más elementos, lo tratamos recursivamente
                    traverseElement(childElement, rowMap, key + ".", headersSet);
                }
            }
        }
    }
}
