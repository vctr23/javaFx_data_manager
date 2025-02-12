package org.example.proyecto_javafx_ficheros;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;  // Import the File class
import java.io.IOException;

import java.io.File;
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
    void createDocument(MouseEvent event) {
        //Funcion que cree el documento
        try {
            File myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("Archivo creado: " + myObj.getName());
            } else {
                System.out.println("El archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido un error.");
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarPopUp() {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta que se cierre el pop-up
        popUp.setTitle("Pop-Up");

        Button btnCerrar = new Button("Cerrar");
        btnCerrar.setOnAction(e -> popUp.close());

        VBox layout = new VBox(10, btnCerrar);
        Scene scene = new Scene(layout, 200, 150);

        popUp.setScene(scene);
        popUp.showAndWait(); // Muestra la ventana y espera a que se cierre antes de continuar
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser.setInitialDirectory(new File("src\\main\\java"));
    }
}
