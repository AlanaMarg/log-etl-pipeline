package com.alana;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Conexao {
    public static Connection conectar() throws Exception{
        Properties config = new  Properties();
        config.load(new FileInputStream("config.properties"));

        String url = config.getProperty("db.url");
        String user = config.getProperty("db.user");
        String senha = config.getProperty("db.password");

        return DriverManager.getConnection(url, user, senha);
    }
}
