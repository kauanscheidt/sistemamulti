package br.multitenant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sistemamulti?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root", "root");
    }
}