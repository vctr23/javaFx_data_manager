package file_exporters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class CsvToJsonConverter {
    public static void main(String[] args) {
        String csvFile = "src//main//files//games.csv";
        String jsonFile = "src//main//files//output.json";
        convertCsvToJson(csvFile, jsonFile);
    }

    public static void convertCsvToJson(String csvFile, String jsonFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String[] headers = br.readLine().split(","); // Leer encabezados
            JSONArray jsonArray = new JSONArray();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Asegura que se lean todos los valores, incluso vacíos
                JSONObject jsonObject = new JSONObject();

                for (int i = 0; i < headers.length; i++) {
                    String value = (i < values.length) ? values[i] : "";
                    jsonObject.put(headers[i], value.isEmpty() ? "N/A" : value);
                }
                jsonArray.put(jsonObject);
            }

            try (FileWriter file = new FileWriter(jsonFile)) {
                file.write(jsonArray.toString(2)); // Formato legible
                file.flush();
            }

            System.out.println("Archivo JSON generado: " + jsonFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
