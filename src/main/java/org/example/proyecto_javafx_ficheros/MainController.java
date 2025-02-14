package org.example.proyecto_javafx_ficheros;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    FileChooser fileChooser = new FileChooser();

    @FXML
    private ImageView icon;

    @FXML
    private TextArea textArea;

    @FXML
    void getDocument(MouseEvent event) {
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
    void showPopUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popUp.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/JavaFx_icon.png")));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Filename");

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

    @FXML
    public void changeToFileView(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("File-view.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/JavaFx_icon.png")));
        stage.setScene(new Scene(root));
        stage.setTitle("File view");
        stage.show();

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void changeToAPIView(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("API-view.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/JavaFx_icon.png")));
        stage.setScene(new Scene(root));
        stage.setTitle("API view");
        stage.show();

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void changeToDBView(MouseEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DB-view.fxml"));
        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/JavaFx_icon.png")));
        stage.setScene(new Scene(root));
        stage.setTitle("DB view");
        stage.show();

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
