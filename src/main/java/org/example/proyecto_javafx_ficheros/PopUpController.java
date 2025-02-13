package org.example.proyecto_javafx_ficheros;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PopUpController extends Application {

    @FXML
    private Button btnCerrar;

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
            File myObj = new File(nombreArchivo + ".txt");
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

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Escribe otro nombre");
        alert.setContentText("Ya hay un archivo con ese nombre");

        alert.showAndWait();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        launch();
    }
}
