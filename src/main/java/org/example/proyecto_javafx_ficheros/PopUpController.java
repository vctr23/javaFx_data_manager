package org.example.proyecto_javafx_ficheros;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
            //Aqui se llamara a otro metodo dependiendo del archivo a importar
            if (selectedFile.getName().endsWith(".csv") && comboBox.getValue().equals("csv")) {
                mostrarAlertaExportFileType();
            } else if (selectedFile.getName().endsWith(".xml") && comboBox.getValue().equals("xml")) {
                mostrarAlertaExportFileType();
            } else if (selectedFile.getName().endsWith(".json") && comboBox.getValue().equals("json")) {
                mostrarAlertaExportFileType();
            }
            File myObj = new File(nombreArchivo + "." + comboBox.getValue());
            if (myObj.createNewFile()) {
                System.out.println("Archivo creado: " + myObj.getName());
                cerrarPopUp(actionEvent);
            } else {
                mostrarAlerta();
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
        comboBox.setItems(FXCollections.observableArrayList("csv", "xml", "json"));
    }
}
