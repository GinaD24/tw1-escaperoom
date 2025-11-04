package com.tallerwebi.punta_a_punta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReiniciarDB {
    public static void limpiarBaseDeDatos() {
        try {
            String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            String dbName = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "tallerwebi";
            String dbUser = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "user";
            String dbPassword = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "user";

            String sqlCommands = ""
                    + "SET FOREIGN_KEY_CHECKS = 0; "
                    + "DELETE FROM AcertijoUsuario; "
                    + "ALTER TABLE AcertijoUsuario AUTO_INCREMENT = 1; "
                    + "DELETE FROM Partida; "
                    + "ALTER TABLE Partida AUTO_INCREMENT = 1; "
                    + "DELETE FROM Usuario; "
                    + "ALTER TABLE Usuario AUTO_INCREMENT = 1; "
                    + "INSERT INTO Usuario(id, email, password, rol, activo) "
                    + "VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true); "
                    + "SET FOREIGN_KEY_CHECKS = 1;";

            String comando = String.format(
                    "docker exec mysql-container mysql -u%s -p%s %s -e \"%s\"",
                    dbUser, dbPassword, dbName, sqlCommands
            );

            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", comando});

            // Leer salida y errores para evitar que se bloquee
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Base de datos limpiada exitosamente");
            } else {
                System.err.println("Error al limpiar la base de datos. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error ejecutando script de limpieza: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        limpiarBaseDeDatos();
    }
}
