package org.example.proyecto_javafx_ficheros;

import file_exporters.XmlToCsvConverter;
import file_exporters.XmlToJsonConverter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.proyecto_javafx_ficheros.MainController.fileType;

public class PopUpController implements Initializable {
    private File selectedFile;

    @FXML
    private Button btnCerrar;

    @FXML
    private ComboBox<String> comboBox;

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
            //Aqui se comprueba que no se exporte a un archivo que ya es el formato
            if (selectedFile.getName().endsWith(".csv") && comboBox.getValue().equals("csv")) {
                mostrarAlertaExportFileType();
            } else if (selectedFile.getName().endsWith(".xml") && comboBox.getValue().equals("xml")) {
                mostrarAlertaExportFileType();
            } else if (selectedFile.getName().endsWith(".json") && comboBox.getValue().equals("json")) {
                mostrarAlertaExportFileType();
            } else {
                //Poner aqui el selectedFile.getName().endsWith()
                switch (comboBox.getValue()) {
                    case "json" -> {
                        if (selectedFile.getName().endsWith(".xml")) {

                        }
                    }
                    case "xml" -> {
                        if (selectedFile.getName().endsWith(".json")) {

                        }
                    }
                    case "csv" -> {
                        if (selectedFile.getName().endsWith(".csv")) {

                        }
                    }
                    default -> System.out.println("Error en el case");
                }
                File myObj = new File("src/main/java/outputsFiles/" + nombreArchivo + "." + comboBox.getValue());
                if (myObj.createNewFile()) {
                    System.out.println("Archivo creado: " + myObj.getName());
                    cerrarPopUp(actionEvent);
                }
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error.");
            e.printStackTrace();
        }
    }

    public void setArchivoSeleccionado(File file) {
        this.selectedFile = file;
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
        alert.setHeaderText("Elije otra opción");
        alert.setContentText("El archivo importado ya es de este tipo");

        alert.showAndWait();
    }

    public void getComboBoxValue() {
        System.out.println(comboBox.getValue());
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("El archivo en el comboBox leido es: " + fileType);
        if (fileType.toLowerCase().equals("csv")) {
            comboBox.setItems(FXCollections.observableArrayList("xml", "json"));
        } else if (fileType.toLowerCase().equals("xml")) {
            comboBox.setItems(FXCollections.observableArrayList("csv", "json"));
        } else {
            comboBox.setItems(FXCollections.observableArrayList("csv", "xml"));
        }
    }
}
