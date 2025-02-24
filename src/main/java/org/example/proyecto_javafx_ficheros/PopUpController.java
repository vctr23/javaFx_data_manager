package org.example.proyecto_javafx_ficheros;

import file_exporters.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.example.proyecto_javafx_ficheros.MainController.*;

public class PopUpController implements Initializable {

    private MainController mainController;

    private File selectedFile;

    @FXML
    private Button btnCerrar;

    @FXML
    private ComboBox<String> comboBox;
    private TableView<?> tableView;

    @FXML
    public void cerrarPopUp(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private TextField textFiledNombreArchivo;

    @FXML
    private Button btnGuardar;

    @FXML
    public void createDocument(ActionEvent actionEvent) {
        //Funcion que cree el documento
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        String nombreArchivo = textFiledNombreArchivo.getText();
        try {
            if (this.selectedFile == null && tableView == null) {
                metodoLlamarAPIS(actionEvent);

            } else if (tableView != null) {
                exportDatabase(comboBox.getValue());
            } else
                //Aqui se comprueba que no se exporte a un archivo que ya es el formato
                if (selectedFile.getName().endsWith(".csv") && comboBox.getValue().equals("csv")) {
                    mostrarAlertaExportFileType();
                } else if (selectedFile.getName().endsWith(".xml") && comboBox.getValue().equals("xml")) {
                    mostrarAlertaExportFileType();
                } else if (selectedFile.getName().endsWith(".json") && comboBox.getValue().equals("json")) {
                    mostrarAlertaExportFileType();
                } else {
                    File myObj = new File("src/main/java/outputsFiles/" + nombreArchivo + "." + comboBox.getValue());
                    if (myObj.exists()) {
                        mostrarAlerta();
                    } else {
                        if (myObj.createNewFile()) {
                            System.out.println("Archivo creado: " + myObj.getName());
                            // Llamar a la función mostrarFicheroImportado antes de cerrar el popup
                            mainController.mostrarFicheroImportado(myObj, mainController);
                            cerrarPopUp(actionEvent);
                        }
                        String extension = selectedFile.getName().toLowerCase().substring(selectedFile.getName().toLowerCase().length() - 3);
                        switch (extension) {
                            case "son" -> {
                                if (comboBox.getValue().equals("xml")) {
                                    JsonToXmlConverter.convertJsonToXml(selectedFile.getPath(), myObj.getPath());
                                } else {
                                    JsonToCsvConverter.convertJsonToCsv(selectedFile.getPath(), myObj.getPath());
                                }
                            }
                            case "xml" -> {
                                if (comboBox.getValue().equals("json")) {
                                    XmlToJsonConverter.convertXmlToJson(selectedFile.getPath(), myObj.getPath());
                                } else {
                                    XmlToCsvConverter.convertXmlToCsv(selectedFile.getPath(), myObj.getPath());
                                }
                            }
                            case "csv" -> {
                                if (comboBox.getValue().equals("xml")) {
                                    CsvToXmlConverter.convertCsvToXml(selectedFile.getPath(), myObj.getPath());
                                } else {
                                    CsvToJsonConverter.convertCsvToJson(selectedFile.getPath(), myObj.getPath());
                                }
                            }
                            default -> System.out.println("Error en el case");
                        }

                        //funcion mostrarFicheroImportado
                        mainController.mostrarFicheroImportado(myObj, mainController);

                    }
                }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error.");
            e.printStackTrace();
        }
    }


    private void metodoLlamarAPIS(ActionEvent actionEvent) {
        // LEER el textAreaExport y crear un archivo JSON temporal
        String textContent = mainController.textAreaExport.getText();
        if (textContent.isEmpty()) {
            showError("No hay contenido para exportar.");
            return;
        }

        String tempJsonFileName = "src/main/java/outputsFiles/temp.json";
        cerrarPopUp(actionEvent);
        try (FileWriter fileWriter = new FileWriter(tempJsonFileName)) {
            fileWriter.write(textContent);
        } catch (IOException e) {
            showError("Error al crear el archivo JSON temporal: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Crear el archivo de salida según el tipo seleccionado en el ComboBox
        String extension = comboBox.getValue();
        String nombreArchivo = textFiledNombreArchivo.getText();
        File outputFile = new File("src/main/java/outputsFiles/" + nombreArchivo + "." + extension);


        switch (extension) {
            case "json" -> {
                // El archivo temporal ya es JSON, así que simplemente lo renombramos
                File jsonFile = new File(tempJsonFileName);
                if (jsonFile.renameTo(outputFile)) {
                    System.out.println("Archivo JSON creado exitosamente: " + outputFile.getPath());
                } else {
                    showError("Error al renombrar el archivo JSON temporal.");
                }
            }
            case "xml" -> {
                JsonToXmlConverter.convertJsonToXml(tempJsonFileName, outputFile.getPath());
                System.out.println("Archivo XML creado exitosamente: " + outputFile.getPath());
            }
            case "csv" -> {
                JsonToCsvConverter.convertJsonToCsv(tempJsonFileName, outputFile.getPath());
                System.out.println("Archivo CSV creado exitosamente: " + outputFile.getPath());
            }
            default -> showError("Formato no soportado.");
        }

        // Eliminar el archivo JSON temporal
        new File(tempJsonFileName).delete();
    }

    public void setArchivoSeleccionado(File file) {
        this.selectedFile = file;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Escribe otro nombre");
        alert.setContentText("Ya hay un archivo con ese nombre");

        alert.showAndWait();
    }

    private void mostrarAlertaExportFileType() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Elige otra opción");
        alert.setContentText("El archivo importado ya es de este tipo");

        alert.showAndWait();
    }

    public String getComboBoxValue() {
        System.out.println(comboBox.getValue());
        return comboBox.getValue();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (fileType.equalsIgnoreCase("csv")) {
            comboBox.setItems(FXCollections.observableArrayList("xml", "json"));
        } else if (fileType.equalsIgnoreCase("xml")) {
            comboBox.setItems(FXCollections.observableArrayList("csv", "json"));
        } else if (fileType.equalsIgnoreCase("json")) {
            comboBox.setItems(FXCollections.observableArrayList("csv", "xml"));
        } else {
            comboBox.setItems(FXCollections.observableArrayList("json", "csv", "xml"));
        }
    }

    public static void exportDatabase(String format) {
        switch (format.toLowerCase()) {
            case "xml":
                System.out.println("Exporting to . . . xml");
                break;
            case "json":
                System.out.println("Exporting to . . . json");
                break;
            case "csv":
                System.out.println("Exporting to . . . csv");
                break;
            default:
                throw new IllegalArgumentException("Formato no soportado: " + format);
        }
    }

    public void setTableView(TableView<?> tableView) {
        this.tableView = tableView;
    }
}