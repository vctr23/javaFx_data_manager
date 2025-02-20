module org.example.proyecto_javafx_ficheros {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.sql;
    requires org.json;

    opens org.example.proyecto_javafx_ficheros to javafx.fxml;
    exports org.example.proyecto_javafx_ficheros;
}