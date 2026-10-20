package com.alana;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    static void main(String[] args) throws Exception {
        LogParser parser = new LogParser();
        List<String> linhas = Files.readAllLines(Path.of("data/access.log"));

        List<LogEntry> entries = new ArrayList<>();
        Map<String, Integer> errosPorIp = new HashMap<>(); //guarda o ip e a quantidade de erros

        System.out.println("\n------LEITURA DOS LOGS------");
        for(String linha : linhas) {
            //parser.parse(linha);

            LogEntry entry = parser.parse(linha);

            if(entry == null) {
                System.out.println("Linha inválida ignorada: " + linha);
            } else {
                System.out.println(entry.getIp() + " " + entry.getTimestamp() + " " + entry.getMethod() +
                        " " + entry.getEndpoint() + " " + entry.getStatusCode());

                entries.add(entry); //guarda os logs válidos
            }
        }

        System.out.println("\n-----QUANTIDADE DE LOGS VALIDOS-----");
        System.out.println(entries.size());

        for(LogEntry e : entries) {
            if(e.getStatusCode() >= 400) {
                if(errosPorIp.containsKey(e.getIp())) {
                    errosPorIp.put(e.getIp(), errosPorIp.get(e.getIp()) + 1);
                } else {
                    errosPorIp.put(e.getIp(), 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> lista = new ArrayList<>(errosPorIp.entrySet()); //transforma o hashmap em lista
        lista.sort((a, b) -> b.getValue() - a.getValue()); //ordena por quantidade de erros

        System.out.println("\n------ERROS POR IP------");
        System.out.println(lista);
    }
}
