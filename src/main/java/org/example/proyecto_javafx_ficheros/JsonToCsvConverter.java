package org.example.proyecto_javafx_ficheros;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class JsonToCsvConverter {
    public static void main(String[] args) {
        String jsonFile = "src//main//files//data.json";
        String csvFile = "src//main//files//output.csv";
        convertJsonToCsv(jsonFile, csvFile);
    }

    public static void convertJsonToCsv(String jsonFile, String csvFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(jsonFile));
             FileWriter fw = new FileWriter(csvFile)) {

            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonText.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonText.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("videojuegos");

            if (jsonArray.length() == 0) {
                System.out.println("El archivo JSON está vacío.");
                return;
            }

            // Se toma el primer objeto para obtener las claves como cabeceras
            JSONObject firstObject = jsonArray.getJSONObject(0);
            String[] headers = JSONObject.getNames(firstObject);

            if (headers == null) {
                System.out.println("No hay datos en el JSON.");
                return;
            }

            fw.append(String.join(",", headers)).append("\n");

            // Iterar sobre cada objeto y escribir los valores en el CSV
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                for (int j = 0; j < headers.length; j++) {
                    String value = obj.optString(headers[j], "N/A");
                    fw.append(value);
                    if (j < headers.length - 1) {
                        fw.append(",");
                    }
                }
                fw.append("\n");
            }

            System.out.println("Archivo CSV generado: " + csvFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
