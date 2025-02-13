package org.example.proyecto_javafx_ficheros;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class HelloController implements Initializable {
    FileChooser fileChooser = new FileChooser();


    @FXML
    private TextArea textArea;

    @FXML
    void getDocument(MouseEvent event) {
        //Funcion que lee y muestra el documento seleccionado
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                textArea.appendText(sc.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void mostrarPopUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popup.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Ventana Emergente");

            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait(); // Bloquea la ventana principal hasta que se cierre el pop-up
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("src\\main\\java"));
    }
}
