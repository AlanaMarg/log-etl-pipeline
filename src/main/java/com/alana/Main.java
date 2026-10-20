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

        List<LogEntry> entries = new ArrayList<>(); //guarda os ips válidos
        Map<String, Integer> errosPorIp = new HashMap<>(); //guarda o ip e a quantidade de erros
        Map<String, Integer> acessosPorRota = new HashMap<>(); //guarda o endpoint e acessos por rota

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

        /*FAZER UM RESUMO DOS LOGS COM: quantidade de logs lido, qntd de logs validos, qntd de logs coml erros*/

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

        int limite = Math.min(3, lista.size());
        System.out.println("\n-----TOP 3 IP's COM MAIS ERROS-----");
        for(int i = 0; i < limite; i++) {
            Map.Entry<String, Integer> par = lista.get(i);
            System.out.println((i + 1) + ". " + par.getKey() + " - " + par.getValue() + " erros.");
        }

        for(LogEntry e : entries) {
            if(e.getStatusCode() == 200) {
                if(acessosPorRota.containsKey(e.getEndpoint())) {
                    acessosPorRota.put(e.getEndpoint(), acessosPorRota.get(e.getEndpoint())+ 1);
                } else {
                    acessosPorRota.put(e.getEndpoint(), 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> listaRota = new ArrayList<>(acessosPorRota.entrySet()); //hash em lista
        listaRota.sort((c, d) -> d.getValue() - c.getValue()); //ordenação da lista

        System.out.println("\n-----ROTA MAIS ACESSADA-----");
        Map.Entry<String, Integer> par2 = listaRota.get(0);
        System.out.println(par2.getKey() + " - " + par2.getValue() + " acessos.");

    }
}