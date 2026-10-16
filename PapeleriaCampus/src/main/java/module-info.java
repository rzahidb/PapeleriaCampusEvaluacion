module org.example.papeleriacampus {

    // Componentes de JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Conexión a PostgreSQL
    requires java.sql;
    requires org.postgresql.jdbc;

    // Permitir que FXML acceda al controlador
    opens org.example.papeleriacampus.controller to javafx.fxml;

    // Permitir el acceso a la aplicación principal
    exports org.example.papeleriacampus;
}