package org.example.proyecto_javafx_ficheros;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    FileChooser fileChooser = new FileChooser();

    @FXML
    private ImageView icon;

    @FXML
    private TextArea textArea;

    @FXML
    private TableView tableView;

    @FXML
    void getDocument(MouseEvent event) {
        File file = fileChooser.showOpenDialog(new Stage());
        try {
            Scanner sc = new Scanner(file);
            textArea.clear();
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

    public void getBDD_info() throws SQLException, IOException {
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null && file.getName().endsWith(".db") || file.getName().endsWith(".sql")) {
            // Obtener URL de conexión según el tipo de base de datos
            String dbUrl = getDatabaseUrl(file);

            if (dbUrl == null) {
                showError("Tipo de base de datos no reconocido.");
                return;
            }

            // Obtener credenciales si no es SQLite
            String username = "";
            String password = "";
            if (!dbUrl.startsWith("jdbc:sqlite")) {
                TextInputDialog userDialog = new TextInputDialog();
                userDialog.setTitle("Credenciales");
                userDialog.setHeaderText("Ingrese usuario y contraseña (si aplica)");
                userDialog.setContentText("Usuario:");

                username = userDialog.showAndWait().orElse("");

                TextInputDialog passDialog = new TextInputDialog();
                passDialog.setTitle("Credenciales");
                passDialog.setHeaderText("Ingrese contraseña:");
                passDialog.setContentText("Contraseña:");

                password = passDialog.showAndWait().orElse("");
            }

            // Conectar a la base de datos
            try (Connection con = DriverManager.getConnection(dbUrl, username, password);
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM juegos")) {

                // Mostrar resultados en TableView
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                tableView.getColumns().clear();
                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

                for (int i = 1; i <= columnCount; i++) {
                    final int colIndex = i - 1;
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(metaData.getColumnName(i));
                    column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(colIndex)));
                    tableView.getColumns().add(column);
                }

                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }

                tableView.setItems(data);
            } catch (SQLException e) {
                showError("Error de conexión: " + e.getMessage());
            }
        } else {
            showError("Seleccione un archivo de base de datos válido (.db, .sql).");
        }
    }

    /**
     * Detecta el tipo de base de datos y devuelve la URL de conexión
     */
    private String getDatabaseUrl(File file) throws IOException {
        String fileName = file.getName().toLowerCase();
        String filePath = file.getCanonicalPath();

        if (fileName.endsWith(".db")) {
            return "jdbc:sqlite:" + filePath;
        } else if (fileName.endsWith(".sql")) {
            return "jdbc:mysql://localhost:3306/" + fileName.replace(".sql", ""); // Requires MySQL configuration
        }
        return null; // Desconocido
    }

    /**
     * Muestra un mensaje de error
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ocurrió un problema");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
