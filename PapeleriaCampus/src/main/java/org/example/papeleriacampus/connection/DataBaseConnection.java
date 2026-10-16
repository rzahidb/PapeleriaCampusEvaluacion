package org.example.papeleriacampus.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {

    private DataBaseConnection() {}

    // Establecer conexión con PostgreSQL
    public static Connection getConnection() throws SQLException {

        Properties datos = new Properties();

        try (FileInputStream archivo =
                     new FileInputStream("db.properties")) {

            datos.load(archivo);

        } catch (IOException e) {

            throw new SQLException(
                    "No se pudo cargar la configuración de PostgreSQL.",
                    e
            );
        }

        return DriverManager.getConnection(
                datos.getProperty("url"),
                datos.getProperty("user"),
                datos.getProperty("password")
        );
    }
}