package org.example.proyecto_javafx_ficheros;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btnOpen = new Button("Abrir Pop-Up");

        VBox root = new VBox(10, btnOpen);
        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Ventana Principal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private Button btnCerrar;

    @FXML
    public void cerrarPopUp(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCerrar.getScene().getWindow();
        stage.close();
    }
}
