package file_exporters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CsvToXmlConverter {
    public static void main(String[] args) {
        String csvFile = "src//main//files//data.csv";
        String xmlFile = "src//main//files//output.xml";
        convertCsvToXml(csvFile, xmlFile);
    }

    public static void convertCsvToXml(String csvFile, String xmlFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Records");
            doc.appendChild(rootElement);

            String line;
            String[] headers = br.readLine().split(","); // Leer encabezados

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Asegura que se lean todos los valores, incluso vacíos
                Element record = doc.createElement("Record");
                rootElement.appendChild(record);

                for (int i = 0; i < headers.length; i++) {
                    Element element = doc.createElement(headers[i]);
                    String value = (i < values.length) ? values[i] : "";
                    element.appendChild(doc.createTextNode(value.isEmpty() ? "N/A" : value));
                    record.appendChild(element);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlFile));

            transformer.transform(source, result);

            System.out.println("Archivo XML generado: " + xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
