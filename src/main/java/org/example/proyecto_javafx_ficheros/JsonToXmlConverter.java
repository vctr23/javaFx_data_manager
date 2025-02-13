package org.example.proyecto_javafx_ficheros;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.io.*;

public class JsonToXmlConverter {
    public static void main(String[] args) {
        String jsonFile = "src//main//files//data.json";  // Ruta al archivo JSON
        String xmlFile = "src//main//files//output.xml";  // Ruta de salida para el XML
        convertJsonToXml(jsonFile, xmlFile);
    }

    public static void convertJsonToXml(String jsonFile, String xmlFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile));
             FileWriter fw = new FileWriter(xmlFile)) {

            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonText.append(line);
            }

            // Convertir JSON a un objeto JSONObject
            JSONObject jsonObject = new JSONObject(jsonText.toString());

            // Llamar a la función para envolver los arrays en objetos
            jsonObject = wrapArrays(jsonObject);

            // Convertir el JSONObject modificado a XML
            String xml = XML.toString(jsonObject);

            // Escribir el XML en el archivo
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            fw.write("<root>\n");
            fw.write(xml);
            fw.write("\n</root>");

            System.out.println("Archivo XML generado: " + xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para envolver arrays en objetos correctamente
    public static JSONObject wrapArrays(JSONObject jsonObject) {
        // Recorrer todas las claves del objeto JSON
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONArray) {
                // Si el valor es un array, lo envolvemos en un solo objeto
                JSONArray array = (JSONArray) value;
                JSONObject wrapper = new JSONObject();

                // Colocar todos los elementos del array bajo un solo contenedor
                for (int i = 0; i < array.length(); i++) {
                    wrapper.put(key + "_" + i, array.get(i));  // Crear un elemento individual para cada valor del array
                }

                // Reemplazar el array original por el objeto que lo contiene
                jsonObject.put(key, wrapper);
            } else if (value instanceof JSONObject) {
                // Si el valor es otro objeto JSON, lo procesamos recursivamente
                jsonObject.put(key, wrapArrays((JSONObject) value));
            }
        }
        return jsonObject;
    }
}
