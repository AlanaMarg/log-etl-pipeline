package com.alana;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    static void main(String[] args) throws Exception {
        LogParser parser = new LogParser();
        List<String> linhas = Files.readAllLines(Path.of("data/access.log"));

        for(String linha : linhas) {
            //parser.parse(linha);de

            LogEntry entry = parser.parse(linha);

            if(entry == null) {
                System.out.println("Linha inválida ignorada: " + linha);
            } else {
                System.out.println(entry.getIp() + " " + entry.getTimestamp() + " " + entry.getMethod() +
                        " " + entry.getEndpoint() + " " + entry.getStatusCode());
            }
        }
    }
}
