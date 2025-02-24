package file_exporters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

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

            Object json = new JSONObject(jsonText.toString());
            if (json instanceof JSONObject) {
                json = jsonToJsonArray((JSONObject) json);
            }

            if (json instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) json;
                if (jsonArray.length() == 0) {
                    System.out.println("El archivo JSON está vacío.");
                    return;
                }

                // Obtener todas las claves para usarlas como cabeceras
                Set<String> headers = new HashSet<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    headers.addAll(jsonArray.getJSONObject(i).keySet());
                }

                fw.append(String.join(",", headers)).append("\n");

                // Iterar sobre cada objeto y escribir los valores en el CSV
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    int j = 0;
                    for (String header : headers) {
                        String value = obj.optString(header, "N/A");
                        fw.append(value);
                        if (j < headers.size() - 1) {
                            fw.append(",");
                        }
                        j++;
                    }
                    fw.append("\n");
                }

                System.out.println("Archivo CSV generado: " + csvFile);
            } else {
                System.out.println("El archivo JSON no es válido.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONArray jsonToJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        return jsonArray;
    }
}