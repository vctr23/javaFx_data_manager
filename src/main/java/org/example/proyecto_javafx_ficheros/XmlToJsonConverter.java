package org.example.proyecto_javafx_ficheros;

import org.json.JSONObject;
import org.json.XML;
import java.io.*;

public class XmlToJsonConverter {
    public static void main(String[] args) {
        String xmlFile = "src//main//files//data.xml";  // Ruta del archivo XML
        String jsonFile = "src//main//files//outputXML.json";  // Ruta de salida para el archivo JSON
        convertXmlToJson(xmlFile, jsonFile);
    }

    public static void convertXmlToJson(String xmlFile, String jsonFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(xmlFile));
             FileWriter fw = new FileWriter(jsonFile)) {

            StringBuilder xmlText = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                xmlText.append(line);
            }

            // Convertir el XML a JSONObject
            String xmlString = xmlText.toString();
            JSONObject jsonObject = XML.toJSONObject(xmlString);

            // Escribir el JSONObject resultante en un archivo JSON
            fw.write(jsonObject.toString(4));  // El número 4 es la indentación para hacerlo legible

            System.out.println("Archivo JSON generado: " + jsonFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
